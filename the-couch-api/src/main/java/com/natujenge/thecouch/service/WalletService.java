package com.natujenge.thecouch.service;

import com.natujenge.thecouch.config.Constants;
import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.domain.enums.StatementPeriod;
import com.natujenge.thecouch.repository.*;
import com.natujenge.thecouch.service.mapper.ClientWalletMapper;
import com.natujenge.thecouch.service.notification.NotificationServiceHTTPClient;
import com.natujenge.thecouch.util.NotificationUtil;
import com.natujenge.thecouch.web.rest.dto.ClientWalletDTO;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.request.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class WalletService {


    private final ClientWalletRepository clientWalletRepository;
    private final WalletRepository walletRepository;

    private final NotificationRepository notificationRepository;

    private final ClientBillingAccountRepository clientBillingAccountRepository;



    private final AccountStatementService accountStatementService;

    private final NotificationServiceHTTPClient notificationServiceHTTPClient;

    private final NotificationSettingsService notificationSettingsService;



    private final UserRepository userRepository;
private final  ClientWalletMapper clientWalletMapper;
    private final ModelMapper modelMapper;

    public WalletService(ClientWalletRepository clientWalletRepository, WalletRepository walletRepository, NotificationRepository notificationRepository, ClientBillingAccountRepository clientBillingAccountRepository, AccountStatementService accountStatementService, NotificationServiceHTTPClient notificationServiceHTTPClient, NotificationSettingsService notificationSettingsService, UserRepository userRepository, ClientWalletMapper clientWalletMapper, ModelMapper modelMapper) {
        this.clientWalletRepository = clientWalletRepository;
        this.walletRepository = walletRepository;
        this.notificationRepository = notificationRepository;
        this.clientBillingAccountRepository = clientBillingAccountRepository;
        this.accountStatementService = accountStatementService;
        this.notificationServiceHTTPClient = notificationServiceHTTPClient;
        this.notificationSettingsService = notificationSettingsService;

        this.userRepository = userRepository;
        this.clientWalletMapper = clientWalletMapper;
        this.modelMapper = modelMapper;
    }

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


    // updates the wallet entry with the provided wallet balance
    public void updateWalletBalance(ClientWallet clientWallet, float paymentBalance, String coachName) {
        clientWallet.setWalletBalance(paymentBalance);
        clientWallet.setLastUpdatedBy(coachName);
        clientWalletRepository.save(clientWallet);
        log.info("client wallet updated!");

    }

    public float updateBillingAccountOnPayment(User coach, User client, float clientBalance, float amountIn) {

        // obtain recent record on billing account

        Optional<ClientBillingAccount> optionalClientBillingAccount = clientBillingAccountRepository.
                findFirstByCoachIdAndClientIdOrderByIdDesc(coach.getId(), client.getId());

        if (optionalClientBillingAccount.isEmpty()) {
            throw new IllegalArgumentException("Client Billing Account not found!");
        }

        ClientBillingAccount clientBillingAccountPrevious = optionalClientBillingAccount.get();
        float amountBilled = (clientBillingAccountPrevious.getAmountBilled() != null) ?
                clientBillingAccountPrevious.getAmountBilled() :
                0f;

        // new client Billing Account record
        ClientBillingAccount clientBillingAccount = new ClientBillingAccount();
        clientBillingAccount.setCreatedBy(coach.getFullName());
        clientBillingAccount.setCoach(coach);
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
        accountStatementService.updateAccountStatement(coach, client, amountIn, amountBilled, paymentBalance);
        clientBillingAccountRepository.save(clientBillingAccount);
        return walletBalance;
    }

    // update billing account on payment by organization
    public float updateBillingAccountOnPaymentByOrganization(Organization organization, User client, float clientBalance, float amountIn) {

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
        clientBillingAccount.setCreatedBy(organization.getOrgName());
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
        return walletBalance;
    }


    // create payment by coach
    public ClientWallet createPaymentByCoach(PaymentRequest paymentRequest, User coach) {

        // Obtain Client associated with wallet
        Optional<User> clientOptional = userRepository.findByIdAndAddedById(paymentRequest.getClientId()
                , coach.getId());
        if (clientOptional.isEmpty()) {
            throw new IllegalArgumentException("Client not found!");
        }
        User client = clientOptional.get();


        // obtain previous payment Record
        Optional<ClientWallet> previousWalletRecord = Optional.ofNullable(getClientWalletRecentRecord(paymentRequest.getCoachId(), paymentRequest.getClientId()));
        float prevWalletBalance;
        previousWalletRecord.get();
        prevWalletBalance = previousWalletRecord.get().getWalletBalance();

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
        if (org.isPresent()) {
            clientWallet.setOrganization(coach.getOrganization());
        }
        // Update Payment Details based on balance on clientWallet;
        float walletBalance = prevWalletBalance + paymentRequest.getAmount();


        // update billing account
        // return walletBalance
        Float walletBalanceAfter = updateBillingAccountOnPayment(
                coach, client, walletBalance, paymentRequest.getAmount());
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
        replacementVariables.put("business_name", clientWallet1.getClient().getAddedBy().getBusinessName());

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
        notification.setCoachId(client.getAddedBy().getId());
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

        // Obtain Client associated with wallet
        Optional<User> clientOptional = userRepository.findById(paymentRequest.getClientId());
        if (clientOptional.isEmpty()) {
            throw new IllegalArgumentException("Client not found!");
        }
        User client = clientOptional.get();
        // obtain previous payment Record
        Optional<ClientWallet> previousWalletRecord = Optional.ofNullable(getClientWalletRecentRecord(paymentRequest.getOrganizationId(), paymentRequest.getClientId()));
        float prevWalletBalance;
        if (previousWalletRecord.get().getWalletBalance().equals(null)) {
            prevWalletBalance = 0;
        } else {
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
        clientWallet.setCreatedBy(organization.getOrgName());
        // Update Payment Details based on balance on clientWallet;
        float walletBalance = prevWalletBalance + paymentRequest.getAmount();
        // update billing account
        // return walletBalance
        Float walletBalanceAfter = updateBillingAccountOnPaymentByOrganization(
                organization, client, walletBalance, paymentRequest.getAmount());
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
        replacementVariables.put("business_name", clientWallet1.getClient().getAddedBy().getBusinessName());
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
        notification.setCoachId(client.getAddedBy().getId());
        notification.setClientId(client.getId());
        notification.setCreatedBy(client.getAddedBy().getFullName());
        //TO DO: add logic to save notification to db
        notificationRepository.save(notification);
        return clientWallet1;
        // Map to Dto and return
        //return modelMapper.map(clientWallet1, ClientWalletDto.class);
    }

    // create payment by client
    public ClientWallet createPaymentByClient(PaymentRequest paymentRequest, User client) {
        // Obtain Client associated with wallet
        Optional<User> clientOptional = userRepository.findById(paymentRequest.getClientId());
        if (clientOptional.isEmpty()) {
            throw new IllegalArgumentException("Client not found!");
        }
        User client1 = clientOptional.get();
        // obtain previous payment Record
        Optional<ClientWallet> previousWalletRecord = Optional.ofNullable(getClientWalletRecentRecord(paymentRequest.getOrganizationId(), paymentRequest.getClientId()));
        float prevWalletBalance;
        if (previousWalletRecord.get().getWalletBalance().equals(null)) {
            prevWalletBalance = 0;
        } else {
            prevWalletBalance = previousWalletRecord.get().getWalletBalance();
        }
        ClientWallet clientWallet = new ClientWallet();
        clientWallet.setAmountDeposited(paymentRequest.getAmount());
        clientWallet.setPaymentCurrency(paymentRequest.getPaymentCurrency());
        clientWallet.setModeOfPayment(paymentRequest.getModeOfPayment());
        clientWallet.setExtPaymentRef(paymentRequest.getExtPaymentRef());
        clientWallet.setCreatedAt(LocalDateTime.now());
        clientWallet.setClient(client1);
        clientWallet.setOrganization(client1.getAddedBy().getOrganization());
        // clientWallet Number Generation
        int randNo = (int) ((Math.random() * (99 - 1)) + 1);
        String clientWalletL = String.format("%05d", randNo);
        String clientWalletNo = "PAY" + client1.getAddedBy().getBusinessName().substring(0, 2) +
                client1.getFirstName().charAt(0) + client.getLastName().charAt(0) + "-" + clientWalletL;
        clientWallet.setClientWalletNumber(clientWalletNo);
        // mngmnt
        clientWallet.setCreatedBy(client1.getFullName());
        // Update Payment Details based on balance on clientWallet;
        float walletBalance = prevWalletBalance + paymentRequest.getAmount();
        // update billing account
        // return walletBalance
        Float walletBalanceAfter = updateBillingAccountOnPaymentByClient(
                client1, walletBalance, paymentRequest.getAmount());
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
        replacementVariables.put("business_name", clientWallet1.getClient().getAddedBy().getBusinessName());
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
        notification.setCoachId(client.getAddedBy().getId());
        notification.setClientId(client.getId());
        notification.setSendReason("PAYMENT RECEIVED");
        notification.setCreatedBy(client.getAddedBy().getFullName());
        //TO DO: add logic to save notification to db
        notificationRepository.save(notification);
        return clientWallet1;
        // Map to Dto and return
        //return modelMapper.map(clientWallet1, ClientWalletDto.class);
    }

    private Float updateBillingAccountOnPaymentByClient(User client1, float walletBalance, Float amount) {
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
        clientBillingAccount.setCoach(client1.getAddedBy());
        clientBillingAccount.setOrganization(client1.getAddedBy().getOrganization());
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

        Page<ClientWalletDTO> walletPage;

        // search payments by coach id
        walletPage = clientWalletRepository.findAllByOrganization_id(organizationId, pageable);

        return new ListResponse(walletPage.getContent(),
                walletPage.getTotalPages(), walletPage.getNumberOfElements(),
                walletPage.getTotalElements());
    }

    // Get all payments by coach Id
//    public ListResponse getPaymentsByCoachId(int page, int perPage, Long coachId) {
//        log.info("Get all Payments by Coach id {}", coachId);
//
//        page = page - 1;
//        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
//        Pageable pageable = PageRequest.of(page, perPage, sort);
//
//        Page<ClientWalletDTO> walletPage;
//
//        // search payments by coach id
//        walletPage = clientWalletRepository.findAllByCoach_id(coachId, pageable);
//
//
//        return new ListResponse(walletPage.getContent(),
//                walletPage.getTotalPages(), walletPage.getNumberOfElements(),
//                walletPage.getTotalElements());
//    }

    // Get all payments by organization Id
    public ListResponse getPaymentsByClientId(int page, int perPage, Long clientId) {
        log.info("Get all Payments by Client id {}", clientId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDTO> walletPage;

        // search payments by coach id
        walletPage = clientWalletRepository.findAllByClient_id(clientId, pageable);

        return new ListResponse(walletPage.getContent(),
                walletPage.getTotalPages(), walletPage.getNumberOfElements(),
                walletPage.getTotalElements());
    }

    public ListResponse getPaymentsByOrganizationIdAndClientId(int page, int perPage, Long organizationId, Long clientId) {
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDTO> paymentPage = clientWalletRepository.findByOrganizationIdAndClientId(
                organizationId,
                clientId,
                pageable
        );
        return new ListResponse(paymentPage.getContent(), paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                paymentPage.getTotalElements());
    }

    public ListResponse getPaymentsByCoachIdIdAndClientId(int page, int perPage, Long coachId, Long clientId) {
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDTO> paymentPage = clientWalletRepository.findByOrganizationIdAndClientId(
                coachId,
                clientId,
                pageable
        );
        return new ListResponse(paymentPage.getContent(), paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                paymentPage.getTotalElements());
    }

    public ListResponse filterByClientNameAndDate(int page, int perPage, String name, LocalDate date) {


        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);


        Page<ClientWalletDTO> receiptPage = null;
        if (name != null && date != null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            receiptPage = clientWalletRepository.findBy(qClientWallet.client.fullName.containsIgnoreCase(name).and(qClientWallet.createdAt.eq(LocalDate.from(date.atStartOfDay()).atStartOfDay())), q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
            return new ListResponse(receiptPage.getContent(), receiptPage.getTotalPages(), receiptPage.getNumberOfElements(), receiptPage.getTotalElements());
        }
        if (name != null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            receiptPage = clientWalletRepository.findBy(qClientWallet.client.fullName.containsIgnoreCase(name), q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
            return new ListResponse(receiptPage.getContent(), receiptPage.getTotalPages(), receiptPage.getNumberOfElements(), receiptPage.getTotalElements());
        }
        if (date != null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            receiptPage = (Page<ClientWalletDTO>) clientWalletRepository.findBy(qClientWallet.createdAt.eq(LocalDate.from(date.atStartOfDay()).atStartOfDay()), q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
            return new ListResponse(receiptPage.getContent(), receiptPage.getTotalPages(), receiptPage.getNumberOfElements(), receiptPage.getTotalElements());
        }

        return new ListResponse(receiptPage.getContent(), receiptPage.getTotalPages(), receiptPage.getNumberOfElements(), receiptPage.getTotalElements());
    }

    //     Get all payments by coach Id and statement period
    public ListResponse getPaymentsByCoachIdAndStatementPeriod(int page, int perPage, Long coachId, StatementPeriod statementPeriod) {
        log.info("Get account statement by coach Id{} and statement period{}", coachId, statementPeriod);


        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDTO> paymentPage;
        // GET BY STATEMENT PERIOD
        if (statementPeriod == StatementPeriod.PER_MONTH) {
            paymentPage = clientWalletRepository.findAllByCoach_idAndCreatedAtBetween(coachId,
                    LocalDateTime.now().minusMonths(1), LocalDateTime.now(), pageable);
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        } else if (statementPeriod == StatementPeriod.PER_6_MONTHS) {
            paymentPage = clientWalletRepository.findAllByCoach_idAndCreatedAtBetween(coachId,
                    LocalDateTime.now().minusWeeks(1), LocalDateTime.now(), pageable);
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        } else if (statementPeriod == StatementPeriod.PER_YEAR) {
            paymentPage = clientWalletRepository.findAllByCoach_idAndCreatedAtBetween(coachId,
                    LocalDateTime.now().minusDays(1), LocalDateTime.now(), pageable);
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        } else {
            paymentPage = clientWalletRepository.findAllByCoach_id(coachId, pageable);
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }
    }

    // Get all payments by organization Id and statement period
    public ListResponse getPaymentsByOrganizationIdAndStatementPeriod(int page, int perPage, Long organizationId, StatementPeriod statementPeriod) {
        log.info("Get account statement by organization Id{} and statement period{}", organizationId, statementPeriod);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);
        // GET BY STATEMENT PERIOD
        if (statementPeriod == StatementPeriod.PER_MONTH) {
            Page<ClientWalletDTO> paymentPage;
            paymentPage = clientWalletRepository.findAllByOrganization_idAndCreatedAtBetween(organizationId,
                    LocalDateTime.now().minusMonths(1), LocalDateTime.now(), pageable);
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        } else if (statementPeriod == StatementPeriod.PER_6_MONTHS) {
            Page<ClientWalletDTO> paymentPage;
            paymentPage = clientWalletRepository.findAllByOrganization_idAndCreatedAtBetween(organizationId,
                    LocalDateTime.now().minusWeeks(1), LocalDateTime.now(), pageable);
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        } else if (statementPeriod == StatementPeriod.PER_YEAR) {
            Page<ClientWalletDTO> paymentPage;
            paymentPage = clientWalletRepository.findAllByOrganization_idAndCreatedAtBetween(organizationId,
                    LocalDateTime.now().minusDays(1), LocalDateTime.now(), pageable);
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        } else {
            Page<ClientWalletDTO> paymentPage;
            paymentPage = clientWalletRepository.findAllByOrganization_id(organizationId, pageable);
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }
    }

    // Get all payments by client Id and statement period
    public ListResponse getPaymentsByClientIdAndStatementPeriod(int page, int perPage, Long clientId, StatementPeriod statementPeriod) {
        log.info("Get account statement by client Id{} and statement period{}", clientId, statementPeriod);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);
        // GET BY STATEMENT PERIOD
        if (statementPeriod == StatementPeriod.PER_MONTH) {
            Page<ClientWalletDTO> paymentPage;
            paymentPage = clientWalletRepository.findAllByClient_idAndCreatedAtBetween(clientId,
                    LocalDateTime.now().minusMonths(1), LocalDateTime.now(), pageable);
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        } else if (statementPeriod == StatementPeriod.PER_6_MONTHS) {
            Page<ClientWalletDTO> paymentPage;
            paymentPage = clientWalletRepository.findAllByClient_idAndCreatedAtBetween(clientId,
                    LocalDateTime.now().minusWeeks(1), LocalDateTime.now(), pageable);
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        } else if (statementPeriod == StatementPeriod.PER_YEAR) {
            Page<ClientWalletDTO> paymentPage;
            paymentPage = clientWalletRepository.findAllByClient_idAndCreatedAtBetween(clientId,
                    LocalDateTime.now().minusDays(1), LocalDateTime.now(), pageable);
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        } else {
            Page<ClientWalletDTO> paymentPage;
            paymentPage = clientWalletRepository.findAllByClient_id(clientId, pageable);
            return new ListResponse(paymentPage.getContent(),
                    paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                    paymentPage.getTotalElements());
        }
    }

    public ClientWallet createWallet(long coachId, long clientId) {
        log.info("Create wallet for coach Id{} and client Id{}", coachId, clientId);
        ClientWallet clientWallet = new ClientWallet();
        clientWallet.setCoach(userRepository.findById(coachId).orElseThrow(IllegalCallerException::new));
        clientWallet.setClient(userRepository.findById(clientId).orElseThrow(IllegalArgumentException::new));
        clientWallet.setOrganization(clientWallet.getClient().getOrganization());
        clientWallet.setWalletBalance(0.0F);
        clientWallet.setCreatedAt(LocalDateTime.now());
        clientWallet.setLastUpdatedAt(LocalDate.from(LocalDateTime.now()));
        clientWalletRepository.save(clientWallet);
        return clientWallet;
    }

    private ClientWallet createExample(Long coachId, Long clientId, String statementPeriod, Long organizationId, String search) {
        ClientWallet walletExample = new ClientWallet();
        User client = new User();
        User coach = new User();

        if (coachId != null) {
            walletExample.setCoach(coach);
            walletExample.getCoach().setId(coachId);
        }
        if (clientId != null) {
            walletExample.setClient(client);
            walletExample.getClient().setId(clientId);
        }


        if (statementPeriod != null && !statementPeriod.isEmpty()) {
            LocalDateTime startDate;
            LocalDateTime endDate = LocalDateTime.now(); // Default to the current date and time

            if (statementPeriod.equalsIgnoreCase("PER_MONTH")) {
                startDate = endDate.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            } else if (statementPeriod.equalsIgnoreCase("PER_6_MONTHS")) {
                startDate = endDate.minusMonths(6).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            } else if (statementPeriod.equalsIgnoreCase("PER_YEAR")) {
                startDate = endDate.minusYears(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            } else {
                startDate = endDate.minusDays(29).withHour(0).withMinute(0).withSecond(0);
            }
        }

        if (organizationId != null) {
            walletExample.setOrganization(new Organization());
            walletExample.getOrganization().setId(organizationId);
        }
        if (search != null && !search.isEmpty()) {
            walletExample.setClient(client);
            if (search.contains("@")) {
                walletExample.getClient().setEmail(search);
            } else if (search.startsWith("+") || search.matches("[0-9]+")) {
                walletExample.getClient().setMsisdn(search);
            } else {
                walletExample.getClient().setFullName(search);
            }
        }
        return walletExample;
    }


    // GetPayments

    public Page<ClientWalletDTO> filter(Long coachId, Long clientId, String statementPeriod,  Long organizationId, String search, Pageable pageable) {
        ClientWallet clientWallet = createExample(coachId, clientId,  statementPeriod, organizationId, search);
        log.info("After example {} ", clientWallet);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("coach.locked", "coach.enabled","coach.onboarded", "client.locked", "client.enabled","client.onboarded")
                .withIgnoreNullValues();
        Example<ClientWallet> example = Example.of(clientWallet, matcher);


        return walletRepository.findAll(example, pageable).map(clientWalletMapper::toDto);
    }

}
