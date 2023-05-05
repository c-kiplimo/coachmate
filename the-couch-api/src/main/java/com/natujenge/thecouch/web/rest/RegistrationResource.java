package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Response;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.UserRepository;
import com.natujenge.thecouch.service.CoachService;
import com.natujenge.thecouch.service.RegistrationService;
import com.natujenge.thecouch.service.UserService;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.request.*;
import com.natujenge.thecouch.service.ResponseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping(path = "/api/registration")
@AllArgsConstructor
@Slf4j
public class RegistrationResource {
    private final RegistrationService registrationService;
   // private final ClientService clientService;
    private final CoachService coachService;
    private final ResponseService responseService;
    private final UserService userService;


    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {
        try{
            registrationService.register(registrationRequest);
            return new ResponseEntity<>(new RestResponse(false,"User registered Successfully!"),
                    HttpStatus.OK);

        } catch (Exception e){

            return new ResponseEntity<>(new RestResponse(true,e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path = "confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token){
        try{
            String response = registrationService.confirmToken(token);
            return new ResponseEntity<>(new RestResponse(false,response), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new RestResponse(true,e.getMessage()),
                    HttpStatus.OK);
        }
    }

    @GetMapping(path = "/coach/confirm")
    public ResponseEntity<?> confirmCoach(@RequestParam("token") String token,
                                             @RequestParam("password") String password) {
        try {
            String response = registrationService.confirmCoachToken(token,password);
            return new ResponseEntity<>(new RestResponse(false, response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.OK);
        }
    }

    @PostMapping(path = "/confirmClientToken")
    ResponseEntity<?> updateAndConfirmClientToken(@RequestBody UserTokenConfirmRequest userTokenConfirmRequest){
        log.info("Request to confirm client token and update password");
        try {
            Optional<User> updateClient = userService.confirmClientTokenAndUpdatePassword(userTokenConfirmRequest);

            return new ResponseEntity<>(new RestResponse(false, "CONFIRMED AND PASSWORD UPDATED"), HttpStatus.OK);
        } catch (Exception e) {
            // log.error("Error while Confirming Client and Updating Client password", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //api to confirm coach and update password
    @PostMapping(path = "/confirmCoachToken")
    ResponseEntity<?> updateAndConfirmCoachToken(@RequestBody CoachRequest coachRequest){
        //log.info("Request to confirm coach token and update password");
        try {
            Optional<User> updateCoach;
           // updateCoach = userRepository.confirmCoachTokenAndUpdatePassword(coachRequest);

            return new ResponseEntity<>(new RestResponse(false, "CONFIRMED AND PASSWORD UPDATED"), HttpStatus.OK);
        } catch (Exception e) {
            //log.error("Error while Confirming Coach and Updating Coach password", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "reset")
    public ResponseEntity<?> reset(@RequestParam("msisdn") String msisdn) {
        try{
            String response =registrationService.requestOTP(msisdn , "RESET");
            return new ResponseEntity<>(new RestResponse(false,response),
                    HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>(new RestResponse(true,e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path = "resend")
    public ResponseEntity<?> resend(@RequestParam("msisdn") String msisdn) {
        try{
            String response =registrationService.requestOTP(msisdn,"RESEND");
            return new ResponseEntity<>(new RestResponse(false,response),
                    HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>(new RestResponse(true,e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(path = "forgot")
    public ResponseEntity<?> forgot(@RequestBody ForgotPassword forgotPassword) {
        try{
            String response =registrationService.reset(forgotPassword);
            return new ResponseEntity<>(new RestResponse(false,response),
                    HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>(new RestResponse(true,e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping(path = "contact")
    ResponseEntity<?> getResponse(@RequestBody Response responseRequest) {
        log.info("request to give response");

        try {
            responseService.saveResponse(responseRequest);
            return new ResponseEntity<>(new RestResponse(false,"Response Received Successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
