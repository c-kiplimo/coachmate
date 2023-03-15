package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.UserRole;
import com.natujenge.thecouch.repository.CoachRepository;
import com.natujenge.thecouch.repository.OrganizationRepository;
import com.natujenge.thecouch.repository.UserRepository;
import com.natujenge.thecouch.service.notification.NotificationServiceHTTPClient;
import com.natujenge.thecouch.util.EmailValidator;
import com.natujenge.thecouch.util.NotificationHelper;
import com.natujenge.thecouch.web.rest.request.ForgotPassword;
import com.natujenge.thecouch.web.rest.request.RegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class RegistrationService {
    private final static String EMAIL_NOT_VALID = "EMAIL %s IS NOT VALID";
    private final static String PHONE_NOT_VALID = "PHONE %s IS NOT VALID";
    private final UserService userService;
    private final CoachService coachService;
    private final OrganizationService organizationService;
    private EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    @Autowired
    CoachRepository coachRepository;

    @Autowired
    OrganizationRepository organizationRepository;


    // Register User
    public void register(RegistrationRequest registrationRequest) {
        log.info("Registering new User");
        boolean isValidEmail = emailValidator.test(registrationRequest.getEmail());


        // Exception handling logic here
        if (!isValidEmail) {
            throw new IllegalStateException(String.format(EMAIL_NOT_VALID, registrationRequest.getEmail()));
        }

        Coach coach;
        Organization organization;

        switch (registrationRequest.getUserRole()) {
            case COACH: {
                // CREATE Coach
                coach = new Coach();
                coach.setBusinessName(registrationRequest.getBusinessName());
                coach.setFirstName(registrationRequest.getFirstName());
                coach.setLastName(registrationRequest.getLastName());
                coach.setFullName(registrationRequest.getFirstName() + " " + registrationRequest.getLastName());
                coach.setMsisdn(registrationRequest.getMsisdn());
                coach.setEmailAddress(registrationRequest.getEmail());
                coach.setOrganization(registrationRequest.getOrganization());


                coachService.addNewCoach(coach);

                log.info("Coach registered");
                List<Object> response = userService.signupUser(
                        new User(
                                registrationRequest.getFirstName(),
                                registrationRequest.getLastName(),
                                registrationRequest.getEmail(),
                                registrationRequest.getMsisdn(),
                                registrationRequest.getPassword(),
                                UserRole.COACH,
                                coach

                        )
                );
                try {
                    // Sending Confirmation Token
                    String token = (String) response.get(1);
                    NotificationHelper.sendConfirmationToken(token, "CONFIRM", (User) response.get(0));
                } catch (Exception e) {
                    log.info("Error while sending confirmation token: ", e);
                }
                break;
            }
            case ORGANIZATION: {
                //CREATE A ORGANIZATION

                List<Object> response = userService.signupUser(
                        new User(
                                registrationRequest.getFirstName(),
                                registrationRequest.getLastName(),
                                registrationRequest.getEmail(),
                                registrationRequest.getMsisdn(),
                                registrationRequest.getPassword(),
                                UserRole.ORGANIZATION

                        )
                );
                try {
                    User user = (User) response.get(0);

                    organization = new Organization();
                    organization.setOrgName(registrationRequest.getBusinessName());
                    organization.setEmail(registrationRequest.getEmail());
                    organization.setMsisdn(registrationRequest.getMsisdn());
                    organization.setFirstName(registrationRequest.getFirstName());
                    organization.setSecondName(registrationRequest.getLastName());
                    organization.setFullName(registrationRequest.getFirstName() + " " + registrationRequest.getLastName());
                    organization.setSuperCoachId(user.getId());

                    organizationService.addNewOrganization(organization);
                    log.info("Organization registered");

                    // Sending Confirmation Token
                    String token = (String) response.get(1);
                    NotificationHelper.sendConfirmationToken(token, "CONFIRM", (User) response.get(0));
                } catch (Exception e){
                    log.info("Error while sending confirmation token: ", e);
                }
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + registrationRequest.getUserRole());
        }

    }

    //Register Client as user
    public void registerClientAsUser(Client clientRequest) {
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
                        clientRequest.getPassword(),
                        UserRole.CLIENT
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
