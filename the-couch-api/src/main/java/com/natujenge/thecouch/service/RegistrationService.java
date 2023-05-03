package com.natujenge.thecouch.service;
import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.config.Constants;
import com.natujenge.thecouch.domain.enums.ContentStatus;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.domain.enums.UserRole;
import com.natujenge.thecouch.repository.*;
import com.natujenge.thecouch.service.notification.NotificationServiceHTTPClient;
import com.natujenge.thecouch.util.EmailValidator;
import com.natujenge.thecouch.util.NotificationHelper;
import com.natujenge.thecouch.web.rest.request.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class RegistrationService {
    private final static String USER_EXISTS = "Email %s Taken!";
    private final static String USER_NOT_FOUND_MSG = "user %s not found!";
    private final static String EMAIL_NOT_VALID = "EMAIL %s IS NOT VALID";
    private final static String PHONE_NOT_VALID = "PHONE %s IS NOT VALID";
    private final UserService userService;

    private final OrganizationService organizationService;
    private final NotificationSettingsService notificationSettingsService;
    private EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final OrganizationWalletRepository organizationWalletRepository;

    private final OrganizationBillingAccountService organizationBillingAccountService;

    private final CoachRepository coachRepository;

    private final CoachWalletRepository coachWalletRepository;
    private final CoachBillingAccountService coachBillingAccountService;

    private final OrganizationRepository organizationRepository;


    // Register User
    public void register(RegistrationRequest registrationRequest) {
        log.info("Registering new User");
        boolean isValidEmail = emailValidator.test(registrationRequest.getEmail());

        // Exception handling logic here
        if (!isValidEmail) {
            throw new IllegalStateException(String.format(EMAIL_NOT_VALID, registrationRequest.getEmail()));
        }

        switch (registrationRequest.getUserRole()) {
            case COACH: {
                // CREATE Coach
                User coach = new User();
                coach.setBusinessName(registrationRequest.getBusinessName());
                coach.setFirstName(registrationRequest.getFirstName());
                coach.setLastName(registrationRequest.getLastName());
                coach.setFullName(registrationRequest.getFirstName() + " " + registrationRequest.getLastName());
                coach.setMsisdn(registrationRequest.getMsisdn());
                coach.setEmail(registrationRequest.getEmail());
                coach.setCreatedBy("SELF-REGISTRATION");
                // coach Number Generation
                int randNo = (int) ((Math.random() * (999 - 1)) + 1);
                String coachL = String.format("%05d", randNo);
                String coachNo = coach.getLastName().substring(0, 2) +
                        coach.getFirstName().charAt(0) + coach.getLastName().charAt(0) + "-" + coachL;
                coach.setCoachNumber(coachNo);

                // Encode Password > from spring boot
                String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());

                // Set details
                coach.setPassword(encodedPassword);
                coach.setContentStatus(ContentStatus.ACTIVE);
                coach.setCreatedAt(LocalDateTime.now());



                // save the User in the database
                User savedCoach = userRepository.save(coach);

                log.info("User saved");

                // Generate a Random 6 digit OTP - 0 - 999999
                int randomOTP = (int) ((Math.random() * (999999 - 1)) + 1);
                String token = String.format("%06d", randomOTP);

                ConfirmationToken confirmationToken = new ConfirmationToken(
                        token,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(15), // expires after 15 minutes of generation
                        coach
                );

                confirmationTokenService.saveConfirmationToken(confirmationToken);
                log.info("Confirmation token generated");

                List<Object> response = new ArrayList<>();
                response.add(savedCoach);
                response.add(token);

                // Create Default NotificationSettings for Every User
                // Generate default Templates for all TemplateTypes
                log.info("Creating Default Settings for User");

                // Defaults for All
                NotificationSettingsRequest notificationSettingsRequest = new NotificationSettingsRequest();
                notificationSettingsRequest.setNotificationMode(NotificationMode.SMS);
                notificationSettingsRequest.setNotificationMode(NotificationMode.EMAIL);
                notificationSettingsRequest.setNotificationMode(NotificationMode.SMS_EMAIL);
                notificationSettingsRequest.setNotificationEnable(true);
                notificationSettingsRequest.setSmsDisplayName(Constants.DEFAULT_SMS_SOURCE_ADDRESS);
                notificationSettingsRequest.setEmailDisplayName(registrationRequest.getFirstName() + " " + registrationRequest.getLastName());
                notificationSettingsRequest.setMsisdn(registrationRequest.getMsisdn());
                notificationSettingsRequest.setTillNumber("DEFAULT");
                notificationSettingsRequest.setAccountNumber(registrationRequest.getMsisdn());
                notificationSettingsRequest.setDepositPercentage(30F);

                notificationSettingsRequest.setRescheduleSessionTemplate(Constants.RESCHEDULE_SESSION_TEMPLATE);
                notificationSettingsRequest.setNewContractTemplate(Constants.DEFAULT_NEW_CONTRACT_EMAIL_TEMPLATE);
                notificationSettingsRequest.setNewContractTemplate(Constants.DEFAULT_NEW_CONTRACT_SMS_TEMPLATE);
                notificationSettingsRequest.setPartialBillPaymentTemplate(Constants.DEFAULT_PARTIAL_BILL_PAYMENT_TEMPLATE);
                notificationSettingsRequest.setFullBillPaymentTemplate(Constants.FULL_BILL_PAYMENT_TEMPLATE);
                notificationSettingsRequest.setConductedSessionTemplate(Constants.CONDUCTED_SESSION_TEMPLATE);
                notificationSettingsRequest.setCancelSessionTemplate(Constants.CANCEL_SESSION_TEMPLATE);
                notificationSettingsRequest.setPaymentReminderTemplate(Constants.DEFAULT_PAYMENT_REMINDER_TEMPLATE);

                notificationSettingsRequest.setNewContractEnable(true);
                notificationSettingsRequest.setPartialBillPaymentEnable(true);
                notificationSettingsRequest.setFullBillPaymentEnable(true);
                notificationSettingsRequest.setCancelSessionEnable(true);
                notificationSettingsRequest.setConductedSessionEnable(true);
                notificationSettingsRequest.setRescheduleSessionEnable(true);
                notificationSettingsRequest.setPaymentReminderEnable(true);
                notificationSettingsRequest.setUser(savedCoach);


                NotificationSettings notificationSettings = notificationSettingsService.
                        addNewSettings(notificationSettingsRequest);

                log.info("Notifications Saved Successfully");
               // set contract templates
                ContractTemplatesRequest contractTemplatesRequest = new ContractTemplatesRequest();
                contractTemplatesRequest.setServicesTemplate(Constants.DEFAULT_SERVICES_TEMPLATE);
                contractTemplatesRequest.setNotesTemplate(Constants.DEFAULT_NOTE_TEMPLATE);
                contractTemplatesRequest.setPracticeTemplate(Constants.DEFAULT_PRACTICE_TEMPLATE);
                contractTemplatesRequest.setTerms_and_conditionsTemplate(Constants.DEFAULT_TERMS_AND_CONDITIONS_TEMPLATE);
                contractTemplatesRequest.setUser(savedCoach);
                ContractTemplate contractTemplate =notificationSettingsService.addContractTemplates(contractTemplatesRequest);
                log.info("Contract Templates Saved Successfully");
                User registeredUser = (User) response.get(0);
                registeredUser.setNotificationSettings(notificationSettings);
                registeredUser.setContractTemplate(contractTemplate);
                userService.updateUser(registeredUser);


                log.info("Coach Updated Successfully");

                try {
                    // Sending Confirmation Token
                    String token1 = (String) response.get(1);
                    NotificationHelper.sendConfirmationToken(token1, "CONFIRM", (User) response.get(0));
                } catch (Exception e) {
                    log.info("Error while sending confirmation token: ", e);
                }
                // Create CoachWallet
                CoachWallet coachWallet = new CoachWallet();
                coachWallet.setUser(savedCoach);
                coachWallet.setWalletBalance(0f);
                coachWallet.setCreatedBy(registrationRequest.getMsisdn());
                coachWalletRepository.save(coachWallet);
                log.info("Coach Wallet created Successfully!");

                // Create CoachBillingAccount
                CoachBillingAccount coachBillingAccount = new CoachBillingAccount();
                coachBillingAccount.setCoach(savedCoach);
                coachBillingAccount.setAmountBilled(0f);
                coachBillingAccount.setCreatedBy(registrationRequest.getMsisdn());
                coachBillingAccountService.createBillingAccount(coachBillingAccount);
                log.info("Coach Billing Account created Successfully!");

                break;
            }


            case ORGANIZATION: {
                //CREATE A ORGANIZATION
                Organization organization = new Organization();
                organization.setOrgName(registrationRequest.getBusinessName());
                organization.setEmail(registrationRequest.getEmail());
                organization.setMsisdn(registrationRequest.getMsisdn());
                organization.setCreatedBy("SELF-REGISTRATION");
                Organization registeredOrg = organizationRepository.save(organization);
                log.info("Organization registered");


                // create user and link organization
                List<Object> response = signupUser(
                        new User(
                                registrationRequest.getFirstName(),
                                registrationRequest.getLastName(),
                                registrationRequest.getEmail(),
                                registrationRequest.getMsisdn(),
                                registrationRequest.getPassword(),
                                UserRole.ORGANIZATION,
                                registeredOrg.getUser()
                        )
                );
                try {
                    User user = (User) response.get(0);
                    organizationService.addNewOrganization(organization);
                    log.info("Organization registered by super coach");
                    // Create Default NotificationSettings for Every User
                    // Generate default Templates for all TemplateTypes
                    log.info("Creating Default Settings for User");

                    // Defaults for All
                    NotificationSettingsRequest notificationSettingsRequest = new NotificationSettingsRequest();
                    notificationSettingsRequest.setNotificationMode(NotificationMode.SMS);
                    notificationSettingsRequest.setNotificationMode(NotificationMode.EMAIL);
                    notificationSettingsRequest.setNotificationMode(NotificationMode.SMS_EMAIL);
                    notificationSettingsRequest.setNotificationEnable(true);
                    notificationSettingsRequest.setSmsDisplayName(Constants.DEFAULT_SMS_SOURCE_ADDRESS);
                    notificationSettingsRequest.setEmailDisplayName(registrationRequest.getFirstName() + " " + registrationRequest.getLastName());
                    notificationSettingsRequest.setMsisdn(registrationRequest.getMsisdn());
                    notificationSettingsRequest.setTillNumber("DEFAULT");
                    notificationSettingsRequest.setAccountNumber(registrationRequest.getMsisdn());
                    notificationSettingsRequest.setDepositPercentage(30F);

                    notificationSettingsRequest.setRescheduleSessionTemplate(Constants.RESCHEDULE_SESSION_TEMPLATE);
                    notificationSettingsRequest.setNewContractTemplate(Constants.DEFAULT_NEW_CONTRACT_EMAIL_TEMPLATE);
                    notificationSettingsRequest.setNewContractTemplate(Constants.DEFAULT_NEW_CONTRACT_SMS_TEMPLATE);
                    notificationSettingsRequest.setPartialBillPaymentTemplate(Constants.DEFAULT_PARTIAL_BILL_PAYMENT_TEMPLATE);
                    notificationSettingsRequest.setFullBillPaymentTemplate(Constants.FULL_BILL_PAYMENT_TEMPLATE);
                    notificationSettingsRequest.setConductedSessionTemplate(Constants.CONDUCTED_SESSION_TEMPLATE);
                    notificationSettingsRequest.setCancelSessionTemplate(Constants.CANCEL_SESSION_TEMPLATE);
                    notificationSettingsRequest.setPaymentReminderTemplate(Constants.DEFAULT_PAYMENT_REMINDER_TEMPLATE);

                    notificationSettingsRequest.setNewContractEnable(true);
                    notificationSettingsRequest.setPartialBillPaymentEnable(true);
                    notificationSettingsRequest.setFullBillPaymentEnable(true);
                    notificationSettingsRequest.setCancelSessionEnable(true);
                    notificationSettingsRequest.setConductedSessionEnable(true);
                    notificationSettingsRequest.setRescheduleSessionEnable(true);
                    notificationSettingsRequest.setPaymentReminderEnable(true);
                    notificationSettingsRequest.setOrganization(registeredOrg);


                    NotificationSettings notificationSettings = notificationSettingsService.
                            addNewSettings(notificationSettingsRequest);

                    log.info("Notifications Saved Successfully");
                    // Update User
                    User registeredUser = (User) response.get(0);
                    log.info("User to be updated: " + registeredUser.getUsername());
                    registeredUser.setNotificationSettings(notificationSettings);
                    registeredUser.setOrganization(Optional.of(registeredOrg));
                    userService.updateUser(registeredUser);


                    // Sending Confirmation Token
                    String token = (String) response.get(1);
                    NotificationHelper.sendConfirmationToken(token, "CONFIRM", user);
                } catch (Exception e){
                    log.info("Error while sending confirmation token: ", e);
                }
                OrganizationWallet organizationWallet = new OrganizationWallet();

                organizationWallet.setOrganization(organization);
                organizationWallet.setWalletBalance(Float.valueOf(0));
                organizationWallet.setCreatedBy(organization.getOrgName());
                organizationWalletRepository.save(organizationWallet);
                log.info("Organization Wallet created Successfully!");

                // Create client Billing Account
                OrganizationBillingAccount organizationBillingAccount = new OrganizationBillingAccount();

                organizationBillingAccount.setAmountBilled((float) 0);
                organizationBillingAccount.setCreatedBy(organization.getOrgName());
                organizationBillingAccountService.createBillingAccount(organizationBillingAccount);
                log.info("Organiation Billing Account created Successfully!");
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + registrationRequest.getUserRole());
        }

    }

    public List<Object> signupUser(User user) {
        log.info("Signing up User");
        boolean userEmailExists = userRepository.findByEmail(user.getEmail())
                .isPresent();

        if (userEmailExists) {
            throw new IllegalStateException(String.format(USER_EXISTS, user.getEmail()));
        }


        // Encode Password > from spring boot
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        // Set details
        user.setPassword(encodedPassword);
        user.setContentStatus(ContentStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());



        // save the User in the database
        User user1 = userRepository.save(user);
        log.info("User saved");

        // Generate a Random 6 digit OTP - 0 - 999999
        int randomOTP = (int) ((Math.random() * (999999 - 1)) + 1);
        String token = String.format("%06d", randomOTP);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15), // expires after 15 minutes of generation
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        log.info("Confirmation token generated");

        List<Object> response = new ArrayList<>();
        response.add(user1);
        response.add(token);

        return response;

    }

    //Register org Coach as user
    public void registerCoachAsUser(CoachRequest coachRequest, Organization organization, User savedCoach) {
        log.info("Registering a coach as user");
        boolean isValidEmail = emailValidator.test(coachRequest.getEmail());

        if(!isValidEmail) {
            throw new IllegalStateException(String.format(EMAIL_NOT_VALID, coachRequest.getEmail()));
        }

        User coach = new User();
        coach.setFirstName(coachRequest.getFirstName());
        coach.setLastName(coachRequest.getLastName());
        coach.setFullName(coachRequest.getFirstName() + " " + coachRequest.getLastName());
        coach.setMsisdn(coachRequest.getMsisdn());
        coach.setEmail(coachRequest.getEmail());
        coach.setCreatedBy(coach.getFullName());
        coach.setOrganization(Optional.ofNullable(organization));

        // coach Number Generation
        int randNo = (int) ((Math.random() * (999 - 1)) + 1);
        String coachL = String.format("%05d", randNo);
        String coachNo =  coach.getFirstName().charAt(0) + coach.getLastName().charAt(0) + "-" + coachL;
        coach.setCoachNumber(coachNo);

        //Create Coach User
        List<Object> response = userService.signupCoachAsUser(
                new User(
                        coachRequest.getFirstName(),
                        coachRequest.getLastName(),
                        coachRequest.getEmail(),
                        coachRequest.getMsisdn(),
                        UserRole.COACH,
                        Optional.ofNullable(organization)
                )
        );

       // User savedCoach = coachRepository.save(coach);

        // Create client wallet
        CoachWallet coachWallet = new CoachWallet();


        if(organization != null){
            coachWallet.setOrganization(organization);
        }

        coachWallet.setUser(savedCoach);
        coachWallet.setWalletBalance(Float.valueOf(0));
       // coachWallet.setCreatedBy();
        coachWalletRepository.save(coachWallet);
        log.info("Client Wallet created Successfully!");

        // Create client Billing Account
        CoachBillingAccount coachBillingAccount = new CoachBillingAccount();

        if(organization != null){
            coachBillingAccount.setOrganization(organization);
        }
        coachBillingAccount.setCoach(savedCoach);
        coachBillingAccount.setAmountBilled((float) 0);
        //coachBillingAccount.setCreatedBy(msisdn);
        coachBillingAccountService.createBillingAccount(coachBillingAccount);
        log.info("Client Billing Account created Successfully!");
       // return savedCoach;

        // Create Default NotificationSettings for Every User
        // Generate default Templates for all TemplateTypes
        log.info("Creating Default Settings for User");

        // Defaults for All
        NotificationSettingsRequest notificationSettingsRequest = new NotificationSettingsRequest();
        notificationSettingsRequest.setNotificationMode(NotificationMode.SMS);
        notificationSettingsRequest.setNotificationMode(NotificationMode.EMAIL);
        notificationSettingsRequest.setNotificationMode(NotificationMode.SMS_EMAIL);
        notificationSettingsRequest.setNotificationEnable(true);
        notificationSettingsRequest.setSmsDisplayName(Constants.DEFAULT_SMS_SOURCE_ADDRESS);
        notificationSettingsRequest.setEmailDisplayName(coachRequest.getFirstName() + " " + coachRequest.getLastName());
        notificationSettingsRequest.setMsisdn(coachRequest.getMsisdn());
        notificationSettingsRequest.setTillNumber("DEFAULT");
        notificationSettingsRequest.setAccountNumber(coachRequest.getMsisdn());
        notificationSettingsRequest.setDepositPercentage(30F);

        notificationSettingsRequest.setRescheduleSessionTemplate(Constants.RESCHEDULE_SESSION_TEMPLATE);
        notificationSettingsRequest.setNewContractTemplate(Constants.DEFAULT_NEW_CONTRACT_EMAIL_TEMPLATE);
        notificationSettingsRequest.setNewContractTemplate(Constants.DEFAULT_NEW_CONTRACT_SMS_TEMPLATE);
        notificationSettingsRequest.setPartialBillPaymentTemplate(Constants.DEFAULT_PARTIAL_BILL_PAYMENT_TEMPLATE);
        notificationSettingsRequest.setFullBillPaymentTemplate(Constants.FULL_BILL_PAYMENT_TEMPLATE);
        notificationSettingsRequest.setConductedSessionTemplate(Constants.CONDUCTED_SESSION_TEMPLATE);
        notificationSettingsRequest.setCancelSessionTemplate(Constants.CANCEL_SESSION_TEMPLATE);
        notificationSettingsRequest.setPaymentReminderTemplate(Constants.DEFAULT_PAYMENT_REMINDER_TEMPLATE);

        notificationSettingsRequest.setNewContractEnable(true);
        notificationSettingsRequest.setPartialBillPaymentEnable(true);
        notificationSettingsRequest.setFullBillPaymentEnable(true);
        notificationSettingsRequest.setCancelSessionEnable(true);
        notificationSettingsRequest.setConductedSessionEnable(true);
        notificationSettingsRequest.setRescheduleSessionEnable(true);
        notificationSettingsRequest.setPaymentReminderEnable(true);
        notificationSettingsRequest.setUser(savedCoach);
        notificationSettingsRequest.setOrganization(savedCoach.getOrganization().get());


        NotificationSettings notificationSettings = notificationSettingsService.
                addNewSettings(notificationSettingsRequest);

        log.info("Notifications Saved Successfully");
        // Update User
        User registeredUser = (User) response.get(0);
        registeredUser.setNotificationSettings(notificationSettings);
       // registeredUser.setCoach(savedCoach);
        userService.updateUser(registeredUser);

        //SEnding Confirmation token
        String token = (String) response.get(1);
        //NotificationHelper.sendConfirmationToken(token, "CONFIRM", (User) response.get(0));


        NotificationServiceHTTPClient notificationServiceHTTPClient = new NotificationServiceHTTPClient();
        String subject = "Coach Account Creation";
        String content = "Hello " + coachRequest.getFirstName() + ", use this link to confirm your account and set your password," +
                " http://localhost:4200/confirmcoach/"+response.get(0)+"/"+token;
        notificationServiceHTTPClient.sendEmail(coachRequest.getEmail() ,subject, content, false);
        notificationServiceHTTPClient.sendSMS(coachRequest.getMsisdn(),content,"COACH-1234");
        log.info(content);
        log.info("Coach registered");

    }


    //Register Client as user
    public void registerClientAsUser(ClientRequest clientRequest, Optional<Organization> organization, User saveClient) {
        log.info("Registering a Client as User");
        boolean isValidEmail = emailValidator.test(clientRequest.getEmail());

        if(!isValidEmail) {
            throw new IllegalStateException(String.format(EMAIL_NOT_VALID, clientRequest.getEmail()));
        }

        //Create Client User
        List<Object> response = userService.signupClientAsUser(
                new User(
                        clientRequest.getFirstName(),
                        clientRequest.getLastName(),
                        clientRequest.getEmail(),
                        clientRequest.getMsisdn(),
                        UserRole.CLIENT,
                        organization
                )
        );

        //SEnding Confirmation token
        String token = (String) response.get(1);
        //NotificationHelper.sendConfirmationToken(token, "CONFIRM", (User) response.get(0));

        NotificationServiceHTTPClient notificationServiceHTTPClient = new NotificationServiceHTTPClient();
        String subject = "Your TheCoach Account Has Been Created.";
        String content = "Hey, use this link to confirm your account and set your password," +
                " http://localhost:4200/confirmclient/"+response.get(0)+"/"+token;
        notificationServiceHTTPClient.sendEmail(clientRequest.getEmail(),subject, content, false);
        notificationServiceHTTPClient.sendSMS(clientRequest.getMsisdn(), subject, content, String.valueOf(false));

    }

    // Confirm token
    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(() ->
                new IllegalStateException("Token not Found!"));

        if(confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("Email Already Confirmed!");

        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("Token Expired!");

        }

        confirmationTokenService.setConfirmedAt(token);

        userService.enableAppUser(confirmationToken.getUser().getEmail());

        return "Confirmed! You can now Login to your account";

    }


    public String confirmCoachToken(String token, String password){
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(() ->
                new IllegalStateException("INVALID OTP!"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Phone Number Already Confirmed!");

        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token Expired!");

        }
        User user = confirmationToken.getUser();
        user.setPassword(password);
        userService.confirmCoach(user);

        confirmationTokenService.setConfirmedAt(token);

        return "Account Confirmed, You can Proceed to Login";

    }


    // Request OTP
    public String requestOTP(String msisdn, String resend) {
        log.info("Generating OTP");

        Optional<User> userOptional = userService.findByMsisdn(msisdn);

        if (userOptional.isEmpty()) {
            throw new IllegalStateException(String.format(PHONE_NOT_VALID, msisdn));
        }
        User user = userOptional.get();
        // Generate a Random 6 digit OTP - 0 - 999999
        int randomOTP = (int) ((Math.random() * (999999 - 1)) + 1);
        String token = String.format("%06d", randomOTP);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5), // Expires after 5 minutes
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        log.info("Reset OTP generated");

        // Sending Confirmation OTP
        NotificationHelper.sendConfirmationToken(token,"RESET",user);

        return "OTP SENT TO " + msisdn;
    }

    // Reset Password
    public String reset(ForgotPassword forgotPassword) {
        log.info("Resetting Password");

        Optional<User> userOptional = userService.findByMsisdn(forgotPassword.getMsisdn());

        if (userOptional.isEmpty()) {
            throw new IllegalStateException(String.format(PHONE_NOT_VALID, forgotPassword.getMsisdn()));
        }
        User user = userOptional.get();

        // VERIFY TOKEN
        confirmToken(forgotPassword.getOtp());
        // Add user
        String encodedPassword = passwordEncoder.encode(forgotPassword.getPassword());

        // Set details
        user.setPassword(encodedPassword);

        // save the User in the database
        userRepository.save(user);
        log.info("User Updated Successfully");

        return "PASSWORD CHANGED SUCCESSFULLY";
    }
}
