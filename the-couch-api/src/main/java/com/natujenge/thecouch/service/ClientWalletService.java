package com.natujenge.thecouch.service;

import com.natujenge.thecouch.config.Constants;
import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.domain.enums.StatementPeriod;
import com.natujenge.thecouch.repository.ClientBillingAccountRepository;
import com.natujenge.thecouch.repository.ClientWalletRepository;
import com.natujenge.thecouch.repository.NotificationRepository;
import com.natujenge.thecouch.repository.UserRepository;
import com.natujenge.thecouch.service.notification.NotificationServiceHTTPClient;
import com.natujenge.thecouch.util.NotificationUtil;
import com.natujenge.thecouch.web.rest.dto.ClientWalletDTO;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.request.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class ClientWalletService {


    private final ClientWalletRepository clientWalletRepository;

    private final NotificationRepository notificationRepository;

    private final ClientBillingAccountRepository clientBillingAccountRepository;

    private final UserRepository userRepository;

    private final CoachWalletService coachWalletService;

    private final OrganizationWalletService organizationWalletService;

    private final AccountStatementService accountStatementService;

    private final NotificationServiceHTTPClient notificationServiceHTTPClient;

    private final NotificationSettingsService notificationSettingsService;

    private final ClientWalletRepository walletRepository;
    private final WalletService walletService;


    private final ModelMapper modelMapper;

    public ClientWalletService(ClientWalletRepository clientWalletRepository, NotificationRepository notificationRepository, ClientBillingAccountRepository clientBillingAccountRepository, UserRepository userRepository, CoachWalletService coachWalletService, OrganizationWalletService organizationWalletService, AccountStatementService accountStatementService, NotificationServiceHTTPClient notificationServiceHTTPClient, NotificationSettingsService notificationSettingsService, ClientWalletRepository walletRepository, ModelMapper modelMapper, WalletService walletService) {
        this.clientWalletRepository = clientWalletRepository;
        this.notificationRepository = notificationRepository;
        this.clientBillingAccountRepository = clientBillingAccountRepository;
        this.userRepository = userRepository;
        this.coachWalletService = coachWalletService;
        this.organizationWalletService = organizationWalletService;
        this.accountStatementService = accountStatementService;
        this.notificationServiceHTTPClient = notificationServiceHTTPClient;
        this.notificationSettingsService = notificationSettingsService;
        this.walletRepository = walletRepository;
        this.modelMapper = modelMapper;
        this.walletService = walletService;
    }

    // Obtain the recent wallet account for the given client
    public ClientWallet getClientWalletRecentRecord_(long organizationId, long clientId) {
        log.info("Get client wallet recent record for organization id {} and client id {}", organizationId, clientId);

        Optional<ClientWallet> optionalClientWallet = clientWalletRepository
                .findFirstByOrganizationIdAndClientIdOrderByIdDesc(organizationId, clientId);

        return optionalClientWallet.orElseThrow(() -> new IllegalArgumentException("Specified wallet does not exist!!!"));
    }

    public ClientWallet getClientWalletRecentRecord(long coachId, long clientId) {
        log.info("Get client wallet recent record for organization id {} and client id {}", coachId, clientId);
        // obtain latest payment Record
        Optional<ClientWallet> optionalClientWallet = clientWalletRepository
                .findFirstByCoachIdAndClientIdOrderByIdDesc(coachId, clientId);

        //            throw new IllegalArgumentException("Specified wallet does not exist!!!");
        return optionalClientWallet.orElseGet(ClientWallet::new);

    }



    public void updateWalletBalance(ClientWallet clientWallet, Float paymentBalance, String coachName) {
        Float previousBalance = clientWallet.getWalletBalance();
        clientWallet.setWalletBalanceBefore(previousBalance);
        clientWallet.setWalletBalance(paymentBalance);
        clientWallet.setLastUpdatedBy(coachName);
        clientWalletRepository.save(clientWallet);
        log.info("client wallet updated!");
    }


    public float updateBillingAccountOnPayment(User coach, User client, float clientBalance,float amountIn) {

        // obtain recent record on billing account

        Optional<ClientBillingAccount> optionalClientBillingAccount = clientBillingAccountRepository.
                findFirstByCoachIdAndClientIdOrderByIdDesc(coach.getId(), client.getId());

        if(optionalClientBillingAccount.isEmpty()){
            // create new billing account
            ClientBillingAccount clientBillingAccount = new ClientBillingAccount();
            clientBillingAccount.setCoach(coach);
            clientBillingAccount.setClient(client);
            clientBillingAccount.setCreatedBy(coach.getFullName());
            clientBillingAccount.setLastUpdatedAt(LocalDateTime.now());
            clientBillingAccount.setAmountBilled(amountIn);
            clientBillingAccountRepository.save(clientBillingAccount);
            return clientBalance;
//            throw new IllegalArgumentException("Client Billing Account not found!");
        }
        CoachWallet coachWallet = coachWalletService.getWalletRecentRecord(coach.getId());

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

        float coachWalletBalance = (coachWallet.getWalletBalance() != null) ? coachWallet.getWalletBalance() : 0f;
        // Calculate Balances
        float paymentBalance =0f;
        float walletBalance = 0f;
        if (clientBalance >= amountBilled){
            paymentBalance = clientBalance - amountBilled;
            walletBalance = paymentBalance;
            clientBillingAccount.setAmountBilled(0f);
          /// add to Coach Wallet
            float coachWalletNewBalance = coachWalletBalance + amountBilled;
            coachWalletService.updateWalletBalance(coachWallet, coachWalletNewBalance, coach.getFullName());

        } else if (clientBalance < amountBilled && clientBalance > 0f) {
            paymentBalance = amountBilled-clientBalance;
            walletBalance = 0f;
            clientBillingAccount.setAmountBilled(paymentBalance);
            float coachWalletNewBalance = coachWalletBalance + clientBalance;
            coachWalletService.updateWalletBalance(coachWallet, coachWalletNewBalance, coach.getFullName());

        }
        accountStatementService.updateAccountStatement(coach, client,amountIn,amountBilled,paymentBalance);
        clientBillingAccountRepository.save(clientBillingAccount);
        return walletBalance ;
    }
    private float updateBillingAccountOnPaymentByClient(User client, float clientBalance, Float amount) {
        // obtain recent record on billing account
        Optional<ClientBillingAccount> optionalClientBillingAccount = clientBillingAccountRepository.
                findByClientIdOrderByIdDesc(client.getId());
        if (optionalClientBillingAccount.isEmpty()) {
            throw new IllegalArgumentException("Client Billing Account not found!");
        }
        OrganizationWallet organizationWallet = organizationWalletService.getWalletRecentRecord(client.getOrganization().getId());
        CoachWallet coachWallet = coachWalletService.getWalletRecentRecord(client.getAddedBy().getId());
        ClientBillingAccount clientBillingAccountPrevious = optionalClientBillingAccount.get();
        float amountBilled = (clientBillingAccountPrevious.getAmountBilled() != null) ?
                clientBillingAccountPrevious.getAmountBilled() :
                0f;
        // new client Billing Account record
        ClientBillingAccount clientBillingAccount = new ClientBillingAccount();
        clientBillingAccount.setClient(client);
        clientBillingAccount.setCoach(client.getAddedBy());
        clientBillingAccount.setOrganization(client.getAddedBy().getOrganization());
        clientBillingAccount.setAmountBilled(amountBilled);
        clientBillingAccount.setCreatedBy(client.getFullName());
        clientBillingAccount.setLastUpdatedAt(LocalDateTime.now());
        // save new record
        float organizationWalletBalance = (organizationWallet.getWalletBalance() != null) ? organizationWallet.getWalletBalance() : 0f;
        float coachWalletBalance = (coachWallet.getWalletBalance() != null) ? coachWallet.getWalletBalance() : 0f;
        // Calculate Balances
        float paymentBalance =0f;
        float walletBalance = 0f;
        if (clientBalance >= amountBilled){
            paymentBalance = clientBalance - amountBilled;
            walletBalance = paymentBalance;
            clientBillingAccount.setAmountBilled(0f);
            /// add to Coach Wallet
            float coachWalletNewBalance = coachWalletBalance + amountBilled;
            coachWalletService.updateWalletBalance(coachWallet, coachWalletNewBalance, client.getAddedBy().getFullName());
        } else if (clientBalance < amountBilled && clientBalance > 0f) {
            paymentBalance = amountBilled-clientBalance;
            walletBalance = 0f;
            clientBillingAccount.setAmountBilled(paymentBalance);
            float coachWalletNewBalance = coachWalletBalance + clientBalance;
            coachWalletService.updateWalletBalance(coachWallet, coachWalletNewBalance, client.getAddedBy().getFullName());
        }
        //accountStatementService.updateAccountStatement(coach, client,amountIn,amountBilled,paymentBalance);
        clientBillingAccountRepository.save(clientBillingAccount);
        return walletBalance ;


    }
    private float updateBillingAccountOnPaymentByClient_(User client, float clientBalance, Float amount) {
        // obtain recent record on billing account
        Optional<ClientBillingAccount> optionalClientBillingAccount = clientBillingAccountRepository.
                findByClientIdOrderByIdDesc(client.getId());
        if (optionalClientBillingAccount.isEmpty()) {
            throw new IllegalArgumentException("Client Billing Account not found!");
        }
        OrganizationWallet organizationWallet = organizationWalletService.getWalletRecentRecord(client.getOrganization().getId());
        CoachWallet coachWallet = coachWalletService.getWalletRecentRecord(client.getAddedBy().getId());
        ClientBillingAccount clientBillingAccountPrevious = optionalClientBillingAccount.get();
        float amountBilled = (clientBillingAccountPrevious.getAmountBilled() != null) ?
                clientBillingAccountPrevious.getAmountBilled() :
                0f;
        // new client Billing Account record
        ClientBillingAccount clientBillingAccount = new ClientBillingAccount();
        clientBillingAccount.setClient(client);
        clientBillingAccount.setCoach(client.getAddedBy());
        clientBillingAccount.setOrganization(client.getAddedBy().getOrganization());
        clientBillingAccount.setAmountBilled(amountBilled);
        clientBillingAccount.setCreatedBy(client.getFullName());
        clientBillingAccount.setLastUpdatedAt(LocalDateTime.now());
        // save new record
        float organizationWalletBalance = (organizationWallet.getWalletBalance() != null) ? organizationWallet.getWalletBalance() : 0f;
        // Calculate Balances
        float paymentBalance =0f;
        float walletBalance = 0f;
        if (clientBalance >= amountBilled){
            paymentBalance = clientBalance - amountBilled;
            walletBalance = paymentBalance;
            clientBillingAccount.setAmountBilled(0f);
            /// add to Coach Wallet
            float organizationWalletNewBalance = organizationWalletBalance + amountBilled;
            organizationWalletService.updateWalletBalance(organizationWallet, organizationWalletNewBalance);
        } else if (clientBalance < amountBilled && clientBalance > 0f) {
            paymentBalance = amountBilled - clientBalance;
            walletBalance = 0f;
            clientBillingAccount.setAmountBilled(paymentBalance);
            float organizationWalletNewBalance = organizationWalletBalance + clientBalance;
            organizationWalletService.updateWalletBalance(organizationWallet, organizationWalletNewBalance);
        }
        //accountStatementService.updateAccountStatement(coach, client,amountIn,amountBilled,paymentBalance);
        clientBillingAccountRepository.save(clientBillingAccount);
        return walletBalance ;


    }

    // update billing account on payment by organization
    public float updateBillingAccountOnPaymentByOrganization(Organization organization , User client, float clientBalance,float amountIn) {

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
        OrganizationWallet organizationWallet = organizationWalletService.getWalletRecentRecord(organization.getId());
        // new client Billing Account record
        ClientBillingAccount clientBillingAccount = new ClientBillingAccount();
        clientBillingAccount.setCreatedBy(organization.getOrgName());
        clientBillingAccount.setOrganization(organization);
        clientBillingAccount.setClient(client);
        clientBillingAccount.setLastUpdatedAt(LocalDateTime.now());
        // Get coach's wallet balance
        float organizationWalletBalance = (organizationWallet.getWalletBalance() != null) ? organizationWallet.getWalletBalance() : 0f;

        // Calculate Balances
        float paymentBalance = 0f;
        float walletBalance = 0f;
        if (clientBalance >= amountBilled) {
            paymentBalance = clientBalance - amountBilled;
            walletBalance = paymentBalance;
            clientBillingAccount.setAmountBilled(0f);
            float organizationWalletNewBalance = organizationWalletBalance + amountBilled;
            organizationWalletService.updateWalletBalance(organizationWallet, organizationWalletNewBalance);

        } else if (clientBalance < amountBilled && clientBalance > 0f) {
            paymentBalance = amountBilled - clientBalance;
            walletBalance = 0f;
            clientBillingAccount.setAmountBilled(paymentBalance);
            float organizationWalletNewBalance = organizationWalletBalance + clientBalance;
            organizationWalletService.updateWalletBalance(organizationWallet, organizationWalletNewBalance);

        }
        accountStatementService.updateAccountStatementByOrganization(organization, client, amountIn, amountBilled, paymentBalance);
        clientBillingAccountRepository.save(clientBillingAccount);
        return walletBalance ;
    }




// create payment by coach
    public ClientWallet createPaymentByCoach(PaymentRequest paymentRequest, User coach) {

        // Obtain Client associated with wallet
        Optional<User> clientOptional = userRepository.findByIdAndAddedById(paymentRequest.getClientId()
                ,coach.getId());
        if(clientOptional.isEmpty()){
            throw new IllegalArgumentException("Client not found!");
        }
        User client = clientOptional.get();


        // obtain previous payment Record
        Optional<ClientWallet> previousWalletRecord = Optional.ofNullable(getClientWalletRecentRecord(paymentRequest.getCoachId(), paymentRequest.getClientId()));
        float prevWalletBalance;
        if(previousWalletRecord.get().getWalletBalance() == null){
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

        // update payments table
        Payment payment = new Payment();
        payment.setAmount(paymentRequest.getAmount());
        payment.setBalanceAfter(walletBalanceAfter);
        payment.setExtPaymentRef(paymentRequest.getExtPaymentRef());
        payment.setModeOfPayment(paymentRequest.getModeOfPayment());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setClient(client);
        payment.setCoach(coach);


        log.info("Wallet successfully saved");
        log.info("Prep to send sms");
        Map<String, Object> replacementVariables = new HashMap<>();
        replacementVariables.put("client_name", clientWallet1.getClient().getFullName());
        replacementVariables.put("amountDeposited", clientWallet1.getAmountDeposited());
        replacementVariables.put("amountBilled", clientWallet1.getWalletBalance());
        replacementVariables.put("business_name",clientWallet1.getClient().getAddedBy().getBusinessName());

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
        try {
            log.info("Organization");
            Optional<User> clientOptional = userRepository.findByIdAndOrganizationId(paymentRequest.getClientId(), organization.getId());
            if (clientOptional.isEmpty()) {
                throw new IllegalArgumentException("Client not found!");
            }
            User client = clientOptional.get();

            // obtain previous payment record
            Optional<ClientWallet> previousWalletRecord = Optional.ofNullable(getClientWalletRecentRecord_(paymentRequest.getOrganizationId(), paymentRequest.getClientId()));
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
            clientWallet.setCreatedBy(organization.getOrgName());
            Optional<Organization> org = Optional.ofNullable(client.getOrganization());
            if (org.isPresent()) {
                clientWallet.setOrganization(client.getOrganization());
            }

            // Update Payment Details based on balance on clientWallet;
            float walletBalance = prevWalletBalance + paymentRequest.getAmount();

            // update billing account and return wallet balance
            Float walletBalanceAfter = updateBillingAccountOnPaymentByOrganization(
                    organization, client, walletBalance, paymentRequest.getAmount());
            clientWallet.setWalletBalance(walletBalanceAfter);

            // update account statement

            // save wallet
            ClientWallet clientWallet1 = clientWalletRepository.save(clientWallet);
            log.info("Wallet successfully saved");

            // prepare variables for SMS message
            Map<String, Object> replacementVariables = new HashMap<>();
            replacementVariables.put("client_name", clientWallet1.getClient().getFullName());
            replacementVariables.put("amountDeposited", clientWallet1.getAmountDeposited());
            replacementVariables.put("amountBilled", clientWallet1.getWalletBalance());
            replacementVariables.put("organization_name", clientWallet1.getClient().getOrganization().getOrgName());

            String smsTemplate = Constants.DEFAULT_PARTIAL_BILL_PAYMENT_TEMPLATE;
            String smsContent = NotificationUtil.generateContentFromTemplate(smsTemplate, replacementVariables);
            String sourceAddress = Constants.DEFAULT_SMS_SOURCE_ADDRESS; //TO-DO get this value from settings
            String referenceId = clientWallet1.getId().toString();
            String msisdn = clientWallet1.getClient().getMsisdn();

            // send SMS message to client
            notificationServiceHTTPClient.sendSMS(sourceAddress, msisdn, smsContent, referenceId);
            log.info("SMS message sent");

            // send email to client
            notificationServiceHTTPClient.sendEmail(client.getEmail(), "PAYMENT RECEIVED", smsContent, false);
            log.info("Email sent");

            // create and save notification object
            Notification notification = new Notification();
            notification.setNotificationMode(NotificationMode.SMS);
            notification.setDestinationAddress(msisdn);
            notification.setSourceAddress(sourceAddress);
            notification.setContent(smsContent);
            notification.setSendReason("PAYMENT RECEIVED");
            notification.setOrganizationId(client.getOrganization().getId());
            notification.setClientId(client.getId());
            notification.setCreatedBy(client.getOrganization().getOrgName());
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
    public ClientWallet createPaymentByClient(PaymentRequest paymentRequest, User client){
        // Obtain Client associated with wallet
        Optional<User> clientOptional = userRepository.findById(paymentRequest.getClientId());
        if(clientOptional.isEmpty()){
            throw new IllegalArgumentException("Client not found!");
        }
        User client1 = clientOptional.get();
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
        clientWallet.setClient(client);
        clientWallet.setCoach(client1.getAddedBy());
        clientWallet.setOrganization(client1.getOrganization());
        // clientWallet Number Generation
        int randNo = (int) ((Math.random() * (99 - 1)) + 1);
        String clientWalletL = String.format("%05d", randNo);
        String clientWalletNo = "PAY" + client1.getAddedBy().getBusinessName().substring(0, 2) +
                client1.getFirstName().charAt(0) + client.getLastName().charAt(0) + "-" + clientWalletL;
        clientWallet.setClientWalletNumber(clientWalletNo);
        // mngmnt

        // Update Payment Details based on balance on clientWallet;
        float walletBalance = prevWalletBalance + paymentRequest.getAmount();
        // update billing account
        // return walletBalance
        if(client.getCreatedBy()==client.getAddedBy().getFullName()){
        Float walletBalanceAfter = updateBillingAccountOnPaymentByClient(
               client1,walletBalance,paymentRequest.getAmount());
            clientWallet.setWalletBalance(walletBalanceAfter);
        }else {
            Float walletBalanceAfter = updateBillingAccountOnPaymentByClient_(
                    client1,walletBalance,paymentRequest.getAmount());
            clientWallet.setWalletBalance(walletBalanceAfter);
        }


        //update account statement
        // save wallet
        ClientWallet clientWallet1 = clientWalletRepository.save(clientWallet);
        log.info("Wallet successfully saved");
        log.info("Prep to send sms");
        Map<String, Object> replacementVariables = new HashMap<>();
        replacementVariables.put("client_name", clientWallet1.getClient().getFullName());
        replacementVariables.put("amountDeposited", clientWallet1.getAmountDeposited());
        replacementVariables.put("amountBilled", clientWallet1.getWalletBalance());
        replacementVariables.put("business_name",clientWallet1.getClient().getAddedBy().getBusinessName());
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
    public ListResponse getPaymentsByCoachId(int page, int perPage, Long coachId) {
        log.info("Get all Payments by Coach id {}", coachId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDTO> walletPage;

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

        Page<ClientWalletDTO> walletPage;

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

        Page<ClientWalletDTO> paymentPage =  clientWalletRepository.findByOrganizationIdAndClientId(
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

        Page<ClientWalletDTO> paymentPage =  clientWalletRepository.findByOrganizationIdAndClientId(
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


        Page<ClientWalletDTO> receiptPage = null;
        if(name !=null){
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            receiptPage=walletRepository.findBy(qClientWallet.client.fullName.containsIgnoreCase(name),q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
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

        Page<ClientWalletDTO> paymentPage = null;

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
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
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
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
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
                    ,q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
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
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
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
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
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
                    ,q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
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
                    ,q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
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

        Page<ClientWalletDTO> paymentPage = null;

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
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
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
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                                    .and(qClientWallet.client.fullName.containsIgnoreCase(clientName))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
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
                    ,q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
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
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                                    .and(qClientWallet.client.email.containsIgnoreCase(search))
                                    .and(qClientWallet.client.firstName.containsIgnoreCase(search))
                                    .and(qClientWallet.client.lastName.containsIgnoreCase(search))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
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
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
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
                    ,q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
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
                    ,q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
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

        Page<ClientWalletDTO> paymentPage = null;
        if(statementPeriod != null) {
            QClientWallet qClientWallet = QClientWallet.clientWallet;
            switch (statementPeriod) {
                case PER_YEAR:
                    LocalDateTime startDate1Year = LocalDateTime.now().minusYears(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate1Year))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_6_MONTHS:
                    LocalDateTime startDate6Months = LocalDateTime.now().minusMonths(6);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDate6Months))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
                    break;
                case PER_MONTH:
                    LocalDateTime startDateMonth = LocalDateTime.now().minusMonths(1);
                    paymentPage = clientWalletRepository.findBy(qClientWallet.statementPeriod.eq(statementPeriod)
                                    .and(qClientWallet.createdAt.after(startDateMonth))
                            , q -> q.sortBy(sort).as(ClientWalletDTO.class).page(pageable));
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
