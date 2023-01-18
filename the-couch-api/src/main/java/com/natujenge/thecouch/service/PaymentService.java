package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.domain.Payment;
import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.domain.enums.ModeOfPayment;
import com.natujenge.thecouch.domain.enums.SessionStatus;
import com.natujenge.thecouch.repository.PaymentRepository;
import com.natujenge.thecouch.repository.SessionRepository;
import com.natujenge.thecouch.service.notification.NotificationServiceHTTPClient;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.dto.PaymentDto;
import com.natujenge.thecouch.web.rest.dto.SessionDto;
import com.natujenge.thecouch.web.rest.request.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    SessionRepository sessionRepository;

    public ListResponse getAllPayments(int page, int perPage, Long coachId) {
        log.info("Get all Payments by Coach id {}",coachId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<PaymentDto> paymentPage;

        // search payments by coach id
        paymentPage = paymentRepository.findAllByCoach_id(coachId, pageable);

        return new ListResponse(paymentPage.getContent(),
                paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                paymentPage.getTotalElements());
    }

    public Optional<PaymentDto> getPaymentById(Long id, Long coachId) {
        log.info("Get Payment of id {}",id);
        Optional<PaymentDto> payment = paymentRepository.findByIdAndCoachId(id,coachId);

        if (payment.isPresent()) {
            return payment;
        } else {
            throw new IllegalStateException("Payment with " + id + " not found ");
        }
    }

    public Payment createPayment(PaymentRequest paymentRequest, Long coachId) {
        log.info("Creating payment");
        // Get session being paid against
        Optional<Session>  optionalSession = sessionRepository.getSessionByIdAndCoachId(paymentRequest.getSessionId(),
                coachId);
        if(optionalSession.isEmpty()){
            // This should be a message
            log.warn("Session with id {} not found", paymentRequest.getSessionId());
            throw new IllegalArgumentException("Session not found");
        }
        Session session = optionalSession.get();

        SessionStatus sessionStatus = (session.getSessionStatus() == SessionStatus.NEW)?
                SessionStatus.CONFIRMED:session.getSessionStatus();
        // Get Client associated with Session
        Client client = session.getClient();
        log.info("Client with id {} found",client.getId());
        // Get Coach Associated with Client
        Coach coach = client.getCoach();
        log.info("Coach with id {} found",coach.getId());
        // generate unique payment reference number
        String paymentRef = UUID.randomUUID().toString();

        // check if payment is partial or full payment
        boolean isPartialPayment;

        Payment payment = new Payment();
        payment.setPaymentRef(paymentRef);
        payment.setExtPaymentRef(paymentRequest.getExtPaymentRef());
        payment.setModeOfPayment(ModeOfPayment.MANUAL);
        payment.setAmount(paymentRequest.getAmount());

        /*      Obtain the immediate prev payment
         *   If optionalPayment isEmpty() > proceed making first Payment
         * Else get the paymentBalance and update new balance appropriately
         * Also check the overPaid amount if > 0 increment that value
         */
        Optional<Payment> optionalPayment = paymentRepository.findFirstByCoachIdAndSessionIdOrderByIdDesc(
                coachId,paymentRequest.getSessionId());

        float currentBalance;
        // First Payment
        if(optionalPayment.isEmpty()){
            log.info("Recording the first Payment!");
            float amountPaid  = paymentRequest.getAmount();
            float sessionAmount = session.getSessionAmount();
            currentBalance = sessionAmount-amountPaid;
            float overPaid;

            // Update order AmountPaid
            session.setAmountPaid(amountPaid);

            if(currentBalance<0){ // Handling OverPayment
                isPartialPayment = false;

                log.info("OverPayment Detected");
                payment.setBalanceAfter(0f);
                overPaid = currentBalance * -1;
                payment.setOverPayment(overPaid);
            }else{
                isPartialPayment = true;
                payment.setBalanceAfter(currentBalance);
                overPaid = 0f;
                payment.setOverPayment(overPaid);
            }
        }else{ // Subsequent Payments
            log.info("Updating Payment Info!");
            Payment prevPayment = optionalPayment.get();

            float amountPaid  = paymentRequest.getAmount();
            /* Check the prevBalance,if balance is 0, increment overPayment Current balance,
             * Else, calculate balance
             */
            float prevBalance = prevPayment.getBalanceAfter();
            float overPayment = prevPayment.getOverPayment();
            float overPaid;
            currentBalance = prevBalance-amountPaid-overPayment;

            // Update order AmountPaid
            session.setAmountPaid(session.getAmountPaid()+paymentRequest.getAmount());

            // Update Balance
            if (prevBalance > 0 && currentBalance > 0){
                isPartialPayment = true;
                payment.setBalanceAfter(currentBalance);
                overPaid = 0f;
                payment.setOverPayment(overPaid);
            }else{ // overPayment
                isPartialPayment = false;
                payment.setBalanceAfter(0f);
                overPaid = currentBalance * -1;
                payment.setOverPayment(overPaid);
            }
        }
        payment.setNarration(paymentRequest.getNarration());

        // update Order
        session.setSessionStatus(sessionStatus);
        session.setSessionBalance(currentBalance);
        sessionRepository.save(session);

        payment.setSession(session);
        payment.setClient(client);
        payment.setCoach(coach);
        payment.setPaidAt(LocalDateTime.now());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setCreatedBy(coach.getFullName()); // baker initiating payment

        Payment payment1 =  paymentRepository.save(payment);

        // Send out a notification advise

        //replacement to get content
        String smsContent = "We have received a payment of amount " + payment1.getAmount() + " for session " +
                payment1.getSession().getName() + ". Session Balance is "+payment1.getSession().getSessionBalance() +
                " Thank you.";

        // SHORTCODE
        String sourceAddress = "SMSAfrica";
        String msisdn = payment.getClient().getMsisdn();

        log.info("about to send message to customer content: {}, from: {}, to: {}, ref id {}",
                smsContent, sourceAddress, msisdn, paymentRef);

        //send sms
        log.info("Sending notification to client");
        NotificationServiceHTTPClient notificationServiceHTTPClient = new NotificationServiceHTTPClient();
        notificationServiceHTTPClient.sendSMS(sourceAddress, msisdn, smsContent, paymentRef);

        //TODO: Get Delivery Report
        log.info("sms sent ");
        return  payment1;
    }


    public Optional<Payment> updatePayment(Long id, Long coachId, String coachName, PaymentRequest paymentRequest) {
        Optional<Payment> paymentOptional = paymentRepository.getPaymentByIdAndCoachId(id,coachId);
        if (paymentOptional.isPresent()){

            // Get session being paid against
            Optional<Session>  optionalSession = sessionRepository.getSessionByIdAndCoachId(paymentRequest.getSessionId()
                    ,coachId);
            if(optionalSession.isEmpty()){
                log.warn("Session with id {} not found", paymentRequest.getSessionId());
                throw new IllegalArgumentException("Session not found");
            }
            Session session = optionalSession.get();

            log.info("Payment with id {} found",id);
            Payment payment = paymentOptional.get();

            log.info("Updating payment Info!");
            float currentBalance;
            float amountPaid  = paymentRequest.getAmount();
            float sessionAmount = session.getSessionAmount();

            currentBalance = sessionAmount-amountPaid;
            float overPaid;

            // Update order AmountPaid
            session.setAmountPaid(amountPaid);

            if(currentBalance<0){ // Handling OverPayment
                payment.setBalanceAfter(0f);
                overPaid = currentBalance * -1;
                payment.setOverPayment(overPaid);
            }else{
                payment.setBalanceAfter(currentBalance);
                overPaid = 0f;
                payment.setOverPayment(overPaid);
            }
            payment.setAmount(amountPaid);
            payment.setExtPaymentRef(paymentRequest.getExtPaymentRef());
            payment.setNarration(paymentRequest.getNarration());
            payment.setLastUpdatedAt(LocalDateTime.now());
            payment.setLastUpdatedBy(coachName);

            sessionRepository.save(session);
            paymentRepository.save(payment);

            log.info("Updated payment of Id {}",payment.getId());
            return Optional.of(payment);
        }
        return paymentOptional;
    }

    public ListResponse filterByClientIdAndCoachId(int page, int perPage, Long clientId, Long coachId) {
        log.debug(
                "Request to filter payments given coachId : {}, client id : {}",
                coachId,
                clientId

        );
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<PaymentDto> paymentPage =  paymentRepository.findByCoachIdAndClientId(
                coachId,
                clientId,
                pageable
        );
        return new ListResponse(paymentPage.getContent(), paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                paymentPage.getTotalElements());
    }

    public ListResponse filterBySessionIdAndCoachId(int page, int perPage, Long sessionId, Long coachId) {
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        // Check if session exists
        Optional<SessionDto> optionalSession = sessionRepository.findByIdAndCoachId(sessionId,coachId);
        if (optionalSession.isEmpty()){
            throw new IllegalArgumentException("Session not Found");
        }


        Page<PaymentDto> paymentPage =  paymentRepository.findBySessionId(
                sessionId,
                pageable
        );

        return new ListResponse(paymentPage.getContent(), paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                paymentPage.getTotalElements());
    }
}
