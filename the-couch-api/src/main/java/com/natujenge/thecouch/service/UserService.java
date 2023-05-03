package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.ConfirmationToken;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.ContentStatus;
import com.natujenge.thecouch.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "User with Email %s not found!";
    private final static String USER_EXISTS = "Email %s Taken!";

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ConfirmationTokenService confirmationTokenService;

    public void enableAppUser(String email) {

        // Request UserDto rather than all details
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));

        user.setEnabled(true);

    }


    public void confirmCoach(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        // Set details
        user.setPassword(encodedPassword);
        enableAppUser(user.getMsisdn());
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

    //Client as User
    public List<Object> signupClientAsUser(User user) {
        log.info("Signing up User");
        boolean userEmailExists = userRepository.findByEmail(user.getEmail())
                .isPresent();

        if (userEmailExists) {
            throw new IllegalStateException(String.format(USER_EXISTS, user.getEmail()));
        }

        // Encode Password > from spring boot
        //String encodedPassword = passwordEncoder.encode(user.getPassword());

        // Set details
        user.setPassword(user.getPassword());
        user.setContentStatus(ContentStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy("Test");


        // save the User in the database
        User user1 = userRepository.save(user);
        log.info("Client User saved");

        // Generate a Random 6 digit OTP - 0 - 999999
        int randomOTP = (int) ((Math.random() * (999999 - 1)) + 1);
        String token = String.format("%06d", randomOTP);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(60*48), // expires after 2 days of generation
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        log.info("Confirmation token generated");

        List<Object> response = new ArrayList<>();
        response.add(user1.getId());
        response.add(token);

        return response;

    }


    // Coach as user
    public List<Object> signupCoachAsUser(User user ) {
        log.info("Signing up coach as user");
        boolean userEmailExists = userRepository.findByEmail(user.getEmail())
                .isPresent();

        if (userEmailExists) {
            throw new IllegalStateException(String.format(USER_EXISTS, user.getEmail()));
        }

        // Set details
        user.setContentStatus(ContentStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy(user.getMsisdn());


        // save the User in the database
        User user1 = userRepository.save(user);
        log.info("Coach User saved");

        // Generate a Random 6 digit OTP - 0 - 999999
        int randomOTP = (int) ((Math.random() * (999999 - 1)) + 1);
        String token = String.format("%06d", randomOTP);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(60*48),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        log.info("Confirmation token generated");

        List<Object> response = new ArrayList<>();
        response.add(user1.getId());
        response.add(token);

        return response;

    }
    public void updateUser(User registeredUser) {
        log.info("Request to update user : {}", registeredUser.getFirstName());
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findUserByUsername(registeredUser.getUsername()));

        if(optionalUser.isEmpty()){
            throw new IllegalStateException("User with username " + registeredUser.getUsername() + " not found!");
        }

        User user = optionalUser.get();
        user.setNotificationSettings(registeredUser.getNotificationSettings());
        user.setFirstName(registeredUser.getFirstName());

        userRepository.save(user);
        log.info("User Updated Successfully");
    }

    public Optional<User> findByEmail(String email) {
        log.info("Request to find user with email : {}", email);

        Optional<User> user = userRepository.findByEmail(email);
        log.info("Found user : {}", user);
        return user;
    }

    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));

    }

    public Optional<User> findByMsisdn(String msisdn) {
        log.info("Request to find user with phone : {}", msisdn);

        return userRepository.findByMsisdn(msisdn);
    }
}
