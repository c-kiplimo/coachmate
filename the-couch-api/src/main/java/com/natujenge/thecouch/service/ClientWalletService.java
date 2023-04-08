package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.domain.enums.StatementPeriod;
import com.natujenge.thecouch.repository.ClientBillingAccountRepository;
import com.natujenge.thecouch.repository.ClientRepository;
import com.natujenge.thecouch.repository.ClientWalletRepository;
import com.natujenge.thecouch.repository.NotificationRepository;
import com.natujenge.thecouch.service.notification.NotificationServiceHTTPClient;
import com.natujenge.thecouch.util.NotificationUtil;
import com.natujenge.thecouch.web.rest.dto.ClientWalletDto;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.request.PaymentRequest;
import com.natujenge.thecouch.domain.ClientBillingAccount;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.*;

@Slf4j
@Service
public class ClientWalletService {

    @Autowired
    ClientWalletRepository clientWalletRepository;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    ClientBillingAccountService clientBillingAccountService;
    @Autowired
    ClientBillingAccountRepository clientBillingAccountRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountStatementService accountStatementService;
    @Autowired
    private NotificationServiceHTTPClient notificationServiceHTTPClient;
    @Autowired
    NotificationSettingsService notificationSettingsService;
    @Autowired
    ClientWalletRepository walletRepository;

    @Autowired
    ModelMapper modelMapper;

    // Obtain the recent wallet account for the given client
    public ClientWallet getClientWalletRecentRecord(long coachId, long clientId) {
log.info("Get client wallet recent record for coach id {} and client id {}", coachId, clientId);
        // obtain latest payment Record
        Optional<ClientWallet> optionalClientWallet = clientWalletRepository.
                findFirstByCoachIdAndClientIdOrderByIdDesc(
                        coachId, clientId);

        if (optionalClientWallet.isEmpty()) {
            throw new IllegalArgumentException("Specified wallet does not exist!!!");

        }

        return optionalClientWallet.get();
    }

    public ClientWallet getClientWalletRecentRecordByOrganization(long organizationId, long clientId) {
        log.info("Get client wallet recent record for organization id {} and client id {}", organizationId, clientId);
        // obtain latest payment Record
        Optional<ClientWallet> optionalClientWallet = clientWalletRepository.
                findFirstByOrganizationIdAndClientIdOrderByIdDesc(
                        organizationId, clientId);

        if (optionalClientWallet.isEmpty()) {
            throw new IllegalArgumentException("Specified wallet does not exist!!!");

        }

        return optionalClientWallet.get();
    }

    public void updateWalletBalance(ClientWallet clientWallet, Float paymentBalance, String coachName) {
        Float previousBalance = clientWallet.getWalletBalance();
        clientWallet.setWalletBalanceBefore(previousBalance);
        clientWallet.setWalletBalance(paymentBalance);
        clientWallet.setLastUpdatedBy(coachName);
        clientWalletRepository.save(clientWallet);
        log.info("client wallet updated!");
    }

    public float updateBillingAccountOnPayment(Coach coach, Client client, float clientBalance,float amountIn) {

        // obtain recent record on billing account

        Optional<ClientBillingAccount> optionalClientBillingAccount = clientBillingAccountRepository.
                findFirstByCoachIdAndClientIdOrderByIdDesc(coach.getId(), client.getId());

        if(optionalClientBillingAccount.isEmpty()){
            throw new IllegalArgumentException("Client Billing Account not found!");
        }

        ClientBillingAccount clientBillingAccountPrevious = optionalClientBillingAccount.get();
        float amountBilled = (clientBillingAccountPrevious.getAmountBilled() != null)?
                clientBillingAccountPrevious.getAmountBilled():
                0f;

        // new client Billing Account record
        ClientBillingAccount clientBillingAccount = new ClientBillingAccount();
        clientBillingAccount.setCreatedBy(coach.getFullName());
        clientBillingAccount.setCoach(coach);
        clientBillingAccount.setClient(client);
        clientBillingAccount.setLastUpdatedAt(LocalDateTime.now());


        // Calculate Balances
        float paymentBalance =0f;
        float walletBalance = 0f;
        if (clientBalance >= amountBilled){
            paymentBalance = clientBalance - amountBilled;
            walletBalance = paymentBalance;
            clientBillingAccount.setAmountBilled(0f);


        } else if (clientBalance < amountBilled && clientBalance > 0f) {
            paymentBalance = amountBilled-clientBalance;
            walletBalance = 0f;
            clientBillingAccount.setAmountBilled(paymentBalance);
        }
        accountStatementService.updateAccountStatement(coach, client,amountIn,amountBilled,paymentBalance);
        clientBillingAccountRepository.save(clientBillingAccount);
        return walletBalance ;
    }
    // update billing account on payment by organization
    public float updateBillingAccountOnPaymentByOrganization(Organization organization , Client client, float clientBalance,float amountIn) {

        // obtain recent record on billing account
        Optional<ClientBillingAccount> optionalClientBillingAccount = clientBillingAccountRepository.
                findFirstByOrganizationIdAndClientIdOrderByIdDesc(organization.getId(), client.getId());
        if (optionalClientBillingAccount.isEmpty()) {
            throw new IllegalArgumentException("Client Billing Account not found!");
        }
        ClientBillingAccount clientBillingAccountPrevious = optionalClientBillingAccount.get();
        float amountBilled = (clientBillingAccountPrevious.getAmountBilled() != null) ?
                clientBillingAccountPrevious.getAmountBilled() :
                0f;
        // new client Billing Account record
        ClientBillingAccount clientBillingAccount = new ClientBillingAccount();
        clientBillingAccount.setCreatedBy(organization.getFullName());
        clientBillingAccount.setOrganization(organization);
        clientBillingAccount.setClient(client);
        clientBillingAccount.setLastUpdatedAt(LocalDateTime.now());
        // Calculate Balances
        float paymentBalance = 0f;
        float walletBalance = 0f;
        if (clientBalance >= amountBilled) {
            paymentBalance = clientBalance - amountBilled;
            walletBalance = paymentBalance;
            clientBillingAccount.setAmountBilled(0f);
        } else if (clientBalance < amountBilled && clientBalance > 0f) {
            paymentBalance = amountBilled - clientBalance;
            walletBalance = 0f;
            clientBillingAccount.setAmountBilled(paymentBalance);
        }
        accountStatementService.updateAccountStatementByOrganization(organization, client, amountIn, amountBilled, paymentBalance);
        clientBillingAccountRepository.save(clientBillingAccount);
        return walletBalance ;
    }




// create payment by coach
    public ClientWallet createPaymentByCoach(PaymentRequest paymentRequest, Coach coach) {

        // Obtain Client associated with wallet
        Optional<Client> clientOptional = clientRepository.findByIdAndCoachId(paymentRequest.getClientId()
                ,coach.getId());
        if(clientOptional.isEmpty()){
            throw new IllegalArgumentException("Client not found!");
        }
        Client client = clientOptional.get();


        // obtain previous payment Record
        Optional<ClientWallet> previousWalletRecord = Optional.ofNullable(getClientWalletRecentRecord(paymentRequest.getCoachId(), paymentRequest.getClientId()));
        float prevWalletBalance;
        if(previousWalletRecord.get().getWalletBalance().equals(null)){
             prevWalletBalance = 0;
        }else {
             prevWalletBalance = previousWalletRecord.get().getWalletBalance();
        }

        ClientWallet clientWallet = new ClientWallet();
        clientWallet.setAmountDeposited(paymentRequest.getAmount());

        clientWallet.setPaymentCurrency(paymentRequest.getPaymentCurrency());
        clientWallet.setModeOfPayment(paymentRequest.getModeOfPayment());
        clientWallet.setExtPaymentRef(paymentRequest.getExtPaymentRef());
        clientWallet.setCreatedAt(LocalDateTime.now());



        clientWallet.setClient(client);
        clientWallet.setCoach(coach);

        // mngmnt
        clientWallet.setCreatedBy(coach.getFullName());
        Optional<Organization> org = Optional.ofNullable(coach.getOrganization());
        if(org.isPresent()) {
            clientWallet.setOrganization(coach.getOrganization());
        }
        // Update Payment Details based on balance on clientWallet;
        float walletBalance = prevWalletBalance + paymentRequest.getAmount();


        // update billing account
        // return walletBalance
        Float walletBalanceAfter = updateBillingAccountOnPayment(
                coach,client,walletBalance,paymentRequest.getAmount());
        clientWallet.setWalletBalance(walletBalanceAfter);

        //update account statement




        // save wallet
        ClientWallet clientWallet1 = clientWalletRepository.save(clientWallet);


        log.info("Wallet successfully saved");
        log.info("Prep to send sms");
        Map<String, Object> replacementVariables = new HashMap<>();
        replacementVariables.put("client_name", clientWallet1.getClient().getFullName());
        replacementVariables.put("amountDeposited", clientWallet1.getAmountDeposited());
        replacementVariables.put("amountBilled", clientWallet1.getWalletBalance());
        replacementVariables.put("business_name",clientWallet1.getClient().getCoach().getBusinessName());

        String smsTemplate = Constants.DEFAULT_PARTIAL_BILL_PAYMENT_TEMPLATE;

        //replacement to get content
        String smsContent = NotificationUtil.generateContentFromTemplate(smsTemplate, replacementVariables);
        String sourceAddress = Constants.DEFAULT_SMS_SOURCE_ADDRESS; //TO-DO get this value from  settings
        String referenceId = clientWallet1.getId().toString();
        String msisdn = clientWallet1.getClient().getMsisdn();

        log.info("about to send message to Client content: {}, from: {}, to: {}, ref id {}", smsContent, sourceAddress, msisdn, referenceId);

        //send sms
        notificationServiceHTTPClient.sendSMS(sourceAddress, msisdn, smsContent, referenceId);
        log.info("sms sent ");

        // sendEmail
        notificationServiceHTTPClient.sendEmail(client.getEmail(),"PAYMENT RECEIVED",smsContent,false);
        log.info("Email sent");


        //create notification object and send it
        Notification notification = new Notification();
        notification.setNotificationMode(NotificationMode.SMS);
        notification.setDestinationAddress(msisdn);
        notification.setSourceAddress(sourceAddress);
        notification.setContent(smsContent);
        notification.setSendReason("PAYMENT RECEIVED");
        notification.setCoachId(client.getCoach().getId());
        notification.setClientId(client.getId());
        notification.setCreatedBy(coach.getFullName());
        //TO DO: add logic to save notification to db

        notificationRepository.save(notification);

        return clientWallet1;
        // Map to Dto and return
        //return modelMapper.map(clientWallet1, ClientWalletDto.class);

    }
    // create payment by organization
    public ClientWallet createPaymentByOrganization(PaymentRequest paymentRequest, Organization organization) {
        try {
            Optional<Client> clientOptional = clientRepository.findByIdAndOrganizationId(paymentRequest.getClientId()
                    ,organization.getId());
            if(clientOptional.isEmpty()){
                throw new IllegalArgumentException("Client not found!");
            }
            Client client = clientOptional.get();


            // obtain previous payment Record
            Optional<ClientWallet> previousWalletRecord = Optional.ofNullable(getClientWalletRecentRecordByOrganization(paymentRequest.getOrganizationId(), paymentRequest.getClientId()));
            float prevWalletBalance;
            if(previousWalletRecord.get().getWalletBalance().equals(null)){
                prevWalletBalance = 0;
            }else {
                prevWalletBalance = previousWalletRecord.get().getWalletBalance();
            }

            ClientWallet clientWallet = new ClientWallet();
            clientWallet.setAmountDeposited(paymentRequest.getAmount());

            clientWallet.setPaymentCurrency(paymentRequest.getPaymentCurrency());
            clientWallet.setModeOfPayment(paymentRequest.getModeOfPayment());
            clientWallet.setExtPaymentRef(paymentRequest.getExtPaymentRef());
            clientWallet.setCreatedAt(LocalDateTime.now());



            clientWallet.setClient(client);
            clientWallet.setOrganization(organization);

            // mngmnt
            clientWallet.setCreatedBy(organization.getFullName());
            Optional<Organization> org = Optional.ofNullable(client.getOrganization());
            if(org.isPresent()) {
                clientWallet.setOrganization(client.getOrganization());
            }
            // Update Payment Details based on balance on clientWallet;
            float walletBalance = prevWalletBalance + paymentRequest.getAmount();


            // update billing account
            // return walletBalance
            Float walletBalanceAfter = updateBillingAccountOnPaymentByOrganization(
                    organization,client,walletBalance,paymentRequest.getAmount());
            clientWallet.setWalletBalance(walletBalanceAfter);

            //update account statement




            // save wallet
            ClientWallet clientWallet1 = clientWalletRepository.save(clientWallet);
            log.info("Wallet successfully saved");
            log.info("Prep to send sms");
            Map<String, Object> replacementVariables = new HashMap<>();
            replacementVariables.put("client_name", clientWallet1.getClient().getFullName());
            replacementVariables.put("amountDeposited", clientWallet1.getAmountDeposited());
            replacementVariables.put("amountBilled", clientWallet1.getWalletBalance());
            replacementVariables.put("business_name", clientWallet1.getClient().getCoach().getBusinessName());
            String smsTemplate = Constants.DEFAULT_PARTIAL_BILL_PAYMENT_TEMPLATE;
            //replacement to get content
            String smsContent = NotificationUtil.generateContentFromTemplate(smsTemplate, replacementVariables);
            String sourceAddress = Constants.DEFAULT_SMS_SOURCE_ADDRESS; //TO-DO get this value from  settings
            String referenceId = clientWallet1.getId().toString();
            String msisdn = clientWallet1.getClient().getMsisdn();
            log.info("about to send message to Client content: {}, from: {}, to: {}, ref id {}", smsContent, sourceAddress, msisdn, referenceId);
            //send sms
            notificationServiceHTTPClient.sendSMS(sourceAddress, msisdn, smsContent, referenceId);
            log.info("sms sent ");
            // sendEmail
            notificationServiceHTTPClient.sendEmail(client.getEmail(), "PAYMENT RECEIVED", smsContent, false);
            log.info("Email sent");
            //create notification object and send it
            Notification notification = new Notification();
            notification.setNotificationMode(NotificationMode.SMS);
            notification.setDestinationAddress(msisdn);
            notification.setSourceAddress(sourceAddress);
            notification.setContent(smsContent);
            notification.setSendReason("PAYMENT RECEIVED");
            notification.setCoachId(client.getCoach().getId());
            notification.setClientId(client.getId());
            notification.setCreatedBy(client.getCoach().getFullName());
            //TO DO: add logic to save notification to db
            notificationRepository.save(notification);
            return clientWallet1;
        }catch (Exception e){
            log.error("An error occurred  {}", e.getMessage());
            throw e;
        }
        // Map to Dto and return
        //return modelMapper.map(clientWallet1, ClientWalletDto.class);
    }
    // create payment by client
    public ClientWallet createPaymentByClient(PaymentRequest paymentRequest, Client client){
        // Obtain Client associated with wallet
        Optional<Client> clientOptional = clientRepository.findById(paymentRequest.getClientId());
        if(clientOptional.isEmpty()){
            throw new IllegalArgumentException("Client not found!");
        }
        Client client1 = clientOptional.get();
        // obtain previous payment Record
        Optional<ClientWallet> previousWalletRecord = Optional.ofNullable(getClientWalletRecentRecord(paymentRequest.getOrganizationId(), paymentRequest.getClientId()));
        float prevWalletBalance;
        if(previousWalletRecord.get().getWalletBalance().equals(null)){
            prevWalletBalance = 0;
        }else {
            prevWalletBalance = previousWalletRecord.get().getWalletBalance();
        }
        ClientWallet clientWallet = new ClientWallet();
        clientWallet.setAmountDeposited(paymentRequest.getAmount());
        clientWallet.setPaymentCurrency(paymentRequest.getPaymentCurrency());
        clientWallet.setModeOfPayment(paymentRequest.getModeOfPayment());
        clientWallet.setExtPaymentRef(paymentRequest.getExtPaymentRef());
        clientWallet.setCreatedAt(LocalDateTime.now());
        clientWallet.setClient(client1);
        clientWallet.setOrganization(client1.getCoach().getOrganization());
        // clientWallet Number Generation
        int randNo = (int) ((Math.random() * (99 - 1)) + 1);
        String clientWalletL = String.format("%05d", randNo);
        String clientWalletNo = "PAY" + client1.getCoach().getBusinessName().substring(0, 2) +
                client1.getFirstName().charAt(0) + client.getLastName().charAt(0) + "-" + clientWalletL;
        clientWallet.setClientWalletNumber(clientWalletNo);
        // mngmnt
        clientWallet.setCreatedBy(client1.getFullName());
        // Update Payment Details based on balance on clientWallet;
        float walletBalance = prevWalletBalance + paymentRequest.getAmount();
        // update billing account
        // return walletBalance
        Float walletBalanceAfter = updateBillingAccountOnPaymentByClient(
               client1,walletBalance,paymentRequest.getAmount());
        clientWallet.setWalletBalance(walletBalanceAfter);
        //update account statement
        // save wallet
        ClientWallet clientWallet1 = clientWalletRepository.save(clientWallet);
        log.info("Wallet successfully saved");
        log.info("Prep to send sms");
        Map<String, Object> replacementVariables = new HashMap<>();
        replacementVariables.put("client_name", clientWallet1.getClient().getFullName());
        replacementVariables.put("amountDeposited", clientWallet1.getAmountDeposited());
        replacementVariables.put("amountBilled", clientWallet1.getWalletBalance());
        replacementVariables.put("business_name",clientWallet1.getClient().getCoach().getBusinessName());
        String smsTemplate = Constants.DEFAULT_PARTIAL_BILL_PAYMENT_TEMPLATE;
        //replacement to get content
        String smsContent = NotificationUtil.generateContentFromTemplate(smsTemplate, replacementVariables);
        String sourceAddress = Constants.DEFAULT_SMS_SOURCE_ADDRESS; //TO-DO get this value from  settings
        String referenceId = clientWallet1.getId().toString();
        String msisdn = clientWallet1.getClient().getMsisdn();
        log.info("about to send message to Client content: {}, from: {}, to: {}, ref id {}", smsContent, sourceAddress, msisdn, referenceId);
        //send sms
        notificationServiceHTTPClient.sendSMS(sourceAddress, msisdn, smsContent, referenceId);

        log.info("sms sent ");
        // sendEmail
        notificationServiceHTTPClient.sendEmail(client.getEmail(),"PAYMENT RECEIVED",smsContent,false);
        log.info("Email sent");
        //create notification object and send it
        Notification notification = new Notification();
        notification.setNotificationMode(NotificationMode.SMS);
        notification.setDestinationAddress(msisdn);
        notification.setSourceAddress(sourceAddress);
        notification.setContent(smsContent);
        notification.setCoachId(client.getCoach().getId());
        notification.setClientId(client.getId());
        notification.setSendReason("PAYMENT RECEIVED");
        notification.setCreatedBy(client.getCoach().getFullName());
        //TO DO: add logic to save notification to db
        notificationRepository.save(notification);
        return clientWallet1;
        // Map to Dto and return
        //return modelMapper.map(clientWallet1, ClientWalletDto.class);
    }

    private Float updateBillingAccountOnPaymentByClient(Client client1, float walletBalance, Float amount) {
        // obtain recent record on billing account
        Optional<ClientBillingAccount> optionalClientBillingAccount = clientBillingAccountRepository.
                findByClientIdOrderByIdDesc(client1.getId());
        if (optionalClientBillingAccount.isEmpty()) {
            throw new IllegalArgumentException("Client Billing Account not found!");
        }
        ClientBillingAccount clientBillingAccountPrevious = optionalClientBillingAccount.get();
        float amountBilled = (clientBillingAccountPrevious.getAmountBilled() != null) ?
                clientBillingAccountPrevious.getAmountBilled() :
                0f;
        // new client Billing Account record
        ClientBillingAccount clientBillingAccount = new ClientBillingAccount();
        clientBillingAccount.setClient(client1);
        clientBillingAccount.setCoach(client1.getCoach());
        clientBillingAccount.setOrganization(client1.getCoach().getOrganization());
        clientBillingAccount.setAmountBilled(amountBilled);
        clientBillingAccount.setCreatedBy(client1.getFullName());
        clientBillingAccount.setLastUpdatedAt(LocalDateTime.now());
        // save new record
        ClientBillingAccount clientBillingAccount1 = clientBillingAccountRepository.save(clientBillingAccount);
        log.info("Client Billing Account successfully saved");
        // update previous record
        clientBillingAccountPrevious.setAmountBilled(walletBalance);
        clientBillingAccountRepository.save(clientBillingAccountPrevious);
        log.info("Client Billing Account successfully updated");
        return walletBalance;


    }

    // Get all payments by organization Id
    public ListResponse getPaymentsByOrganizationId(int page, int perPage, Long organizationId) {
        log.info("Get all Payments by Organization id {}", organizationId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDto> walletPage;

        // search payments by coach id
        walletPage = clientWalletRepository.findAllByOrganization_id(organizationId, pageable);

        return new ListResponse(walletPage.getContent(),
                walletPage.getTotalPages(), walletPage.getNumberOfElements(),
                walletPage.getTotalElements());
    }

    // Get all payments by coach Id
    public ListResponse getPaymentsByCoachId(int page, int perPage, Long coachId) {
        log.info("Get all Payments by Coach id {}", coachId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDto> walletPage;

        // search payments by coach id
        walletPage = clientWalletRepository.findAllByCoach_id(coachId, pageable);

        return new ListResponse(walletPage.getContent(),
                walletPage.getTotalPages(), walletPage.getNumberOfElements(),
                walletPage.getTotalElements());
    }

    // Get all payments by organization Id
    public ListResponse getPaymentsByClientId(int page, int perPage, Long clientId) {
        log.info("Get all Payments by Client id {}", clientId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDto> walletPage;

        // search payments by coach id
        walletPage = clientWalletRepository.findAllByClient_id(clientId, pageable);

        return new ListResponse(walletPage.getContent(),
                walletPage.getTotalPages(), walletPage.getNumberOfElements(),
                walletPage.getTotalElements());
    }

    public ListResponse getPaymentsByOrganizationIdAndClientId(int page, int perPage, Long organizationId,Long clientId) {
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDto> paymentPage =  clientWalletRepository.findByOrganizationIdAndClientId(
                organizationId,
                clientId,
                pageable
        );
        return new ListResponse(paymentPage.getContent(), paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                paymentPage.getTotalElements());
    }

    public ListResponse getPaymentsByCoachIdIdAndClientId(int page, int perPage, Long coachId,Long clientId) {
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDto> paymentPage =  clientWalletRepository.findByOrganizationIdAndClientId(
                coachId,
                clientId,
                pageable
        );
        return new ListResponse(paymentPage.getContent(), paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                paymentPage.getTotalElements());
    }
    public ListResponse filterByClientName(int page, int perPage, String name) {




        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);


        Page<ClientWalletDto> receiptPage = null;
        if(name !=null){
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            receiptPage=walletRepository.findBy(qClientWallet.client.fullName.containsIgnoreCase(name),q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
            return new ListResponse(receiptPage.getContent(), receiptPage.getTotalPages(), receiptPage.getNumberOfElements(), receiptPage.getTotalElements());
        }

        return new ListResponse(receiptPage.getContent(), receiptPage.getTotalPages(), receiptPage.getNumberOfElements(), receiptPage.getTotalElements());
    }

    //     Get all payments by coach Id and statement period
    public ListResponse getPaymentsByCoachIdAndStatementPeriod(int page, int perPage, Long coachId,String search,String clientName,StatementPeriod statementPeriod) {
        log.info("Get account statement by coach Id{} and statement period{}", coachId, statementPeriod);


        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDto> paymentPage = null;

        if (statementPeriod != null && clientName !=null && search != null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            switch (statementPeriod) {
                case PER_YEAR:
                    LocalDateTime startDate1Year = LocalDateTime.now().minusYears(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate1Year))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                default:
                    break;
            }
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }  if (statementPeriod != null && clientName !=null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            switch (statementPeriod) {
                case PER_YEAR:
                    LocalDateTime startDate1Year = LocalDateTime.now().minusYears(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate1Year))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                default:
                    break;
            }
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }  if (clientName !=null && search != null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            paymentPage = clientWalletRepository.findBy(qClientWallet.client.fullName.containsIgnoreCase(clientName).
                            and(qClientWallet.client.email.containsIgnoreCase(search)).
                            and(qClientWallet.client.firstName.containsIgnoreCase(search)).
                            and(qClientWallet.client.lastName.containsIgnoreCase(search))
                    ,q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }if (statementPeriod != null  && search != null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            switch (statementPeriod) {
                case PER_YEAR:
                    LocalDateTime startDate1Year = LocalDateTime.now().minusYears(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate1Year))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                default:
                    break;
            }
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }

        if(statementPeriod != null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            switch (statementPeriod) {
                case PER_YEAR:
                    LocalDateTime startDate1Year = LocalDateTime.now().minusYears(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate1Year))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                default:
                    break;
            }
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }


        if ( clientName !=null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            paymentPage = clientWalletRepository.findBy(qClientWallet.client.fullName.containsIgnoreCase(clientName)
                    ,q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }
        if ( search != null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            paymentPage = clientWalletRepository.findBy(qClientWallet.client.email.containsIgnoreCase
                            (search).
                            and(qClientWallet.client.firstName.containsIgnoreCase(search)).
                            and(qClientWallet.client.lastName.containsIgnoreCase(search))
                    ,q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }
        return new ListResponse(paymentPage.getContent(),
                paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                paymentPage.getTotalElements());
    }
    // Get all payments by organization Id and statement period
    public ListResponse getPaymentsByOrganizationIdAndClientAndStatementPeriod(int page, int perPage, Long organizationId,String search,String clientName,StatementPeriod statementPeriod) {
        log.info("Get account statement by organization Id{} and statement period{}", organizationId, statementPeriod);


        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDto> paymentPage = null;

        if (statementPeriod != null && clientName !=null && search != null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            switch (statementPeriod) {
                case PER_YEAR:
                    LocalDateTime startDate1Year = LocalDateTime.now().minusYears(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate1Year))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                default:
                    break;
            }
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }  if (statementPeriod != null && clientName !=null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            switch (statementPeriod) {
                case PER_YEAR:
                    LocalDateTime startDate1Year = LocalDateTime.now().minusYears(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate1Year))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                default:
                    break;
            }
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }  if (clientName !=null && search != null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            paymentPage = clientWalletRepository.findBy(qClientWallet.client.fullName.containsIgnoreCase(clientName).
                            and(qClientWallet.client.email.containsIgnoreCase(search)).
                            and(qClientWallet.client.firstName.containsIgnoreCase(search)).
                            and(qClientWallet.client.lastName.containsIgnoreCase(search))
                    ,q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }if (statementPeriod != null  && search != null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            switch (statementPeriod) {
                case PER_YEAR:
                    LocalDateTime startDate1Year = LocalDateTime.now().minusYears(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate1Year))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                default:
                    break;
            }
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }

        if(statementPeriod != null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            switch (statementPeriod) {
                case PER_YEAR:
                    LocalDateTime startDate1Year = LocalDateTime.now().minusYears(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate1Year))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                default:
                    break;
            }
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }


        if ( clientName !=null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            paymentPage = clientWalletRepository.findBy(qClientWallet.client.fullName.containsIgnoreCase(clientName)
                    ,q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }
        if ( search != null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            paymentPage = clientWalletRepository.findBy(qClientWallet.client.email.containsIgnoreCase
                                    (search).
                            and(qClientWallet.client.firstName.containsIgnoreCase(search)).
                            and(qClientWallet.client.lastName.containsIgnoreCase(search))
                    ,q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }
        return new ListResponse(paymentPage.getContent(),
                paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                paymentPage.getTotalElements());
    }
    // Get all payments by client Id and statement period
    public ListResponse getPaymentsByClientIdAndStatementPeriod(int page, int perPage, Long clientId,StatementPeriod statementPeriod) {
        log.info("Get account statement by client Id{} and statement period{}", clientId, statementPeriod);


        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDto> paymentPage = null;
        if(statementPeriod != null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            switch (statementPeriod) {
                case PER_YEAR:
                    LocalDateTime startDate1Year = LocalDateTime.now().minusYears(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate1Year))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                            , q -> q.sortBy(sort).as(ClientWalletDto.class).page(pageable));
                    break;
                default:
                    break;
            }
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }
        return new ListResponse(paymentPage.getContent(),
                paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                paymentPage.getTotalElements());
    }


}
