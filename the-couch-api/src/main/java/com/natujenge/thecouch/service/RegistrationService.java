package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.domain.ConfirmationToken;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.UserRole;
import com.natujenge.thecouch.util.EmailValidator;
import com.natujenge.thecouch.web.rest.request.RegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class RegistrationService {
    private final static String EMAIL_NOT_VALID = "EMAIL %s IS NOT VALID";
    private final UserService userService;
    private final CoachService coachService;
    private EmailValidator emailValidator;

    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest registrationRequest) {
        log.info("Registering new User");
        boolean isValidEmail = emailValidator.test(registrationRequest.getEmail());


        // Exception handling logic here
        if(!isValidEmail){
            throw new IllegalStateException(String.format(EMAIL_NOT_VALID,registrationRequest.getEmail()));
        }

        // CREATE Coach
        Coach coach = new Coach();
        coach.setBusinessName(registrationRequest.getBusinessName());
        coach.setFullName( registrationRequest.getFullName());
        coach.setMsisdn(registrationRequest.getMsisdn());
        coach.setEmailAddress(registrationRequest.getEmail());


        coachService.addNewCoach(coach);

        log.info("Baker registered");
        return userService.signupUser(
                new User(
                        registrationRequest.getFullName(),
                        registrationRequest.getEmail(),
                        registrationRequest.getPassword(),
                        UserRole.COACH,
                        coach
                )
        );
    }

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
}
