package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.domain.Response;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.service.ClientService;
import com.natujenge.thecouch.service.CoachService;
import com.natujenge.thecouch.service.RegistrationService;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.request.ClientRequest;
import com.natujenge.thecouch.web.rest.request.CoachRequest;
import com.natujenge.thecouch.web.rest.request.ForgotPassword;
import com.natujenge.thecouch.web.rest.request.RegistrationRequest;
import com.natujenge.thecouch.service.ResponseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping(path = "/api/registration")
@AllArgsConstructor
@Slf4j
public class RegistrationResource {

    private final RegistrationService registrationService;
    private final ClientService clientService;
    private final CoachService coachService;

    private final ResponseService responseService;
    @Autowired
    private ModelMapper modelMapper;


    //api to create a coach by organization
    @PostMapping
    ResponseEntity<?> addNewCoach(@RequestBody CoachRequest coachRequest,
                                   @AuthenticationPrincipal User userDetails) {
        log.info("request to add new coach by organization");
        try {
            Coach newCoach = coachService.addNewCoachByOrganization(coachRequest.getOrgIdId(),
                    coachRequest);
            if (newCoach != null) {
                CoachRequest response = modelMapper.map(newCoach, CoachRequest.class);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new RestResponse(true,
                        "Coach by organization not created"), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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

    //api to confirm client and update password
    @PostMapping(path = "/confirmClientToken")
    ResponseEntity<?> updateAndConfirmClientToken(@RequestBody ClientRequest clientRequest){
       // log.info("Request to confirm client token and update password");
        try {
            Optional<User> updateClient;
            updateClient = clientService.confirmClientTokenAndUpdatePassword(clientRequest);

            return new ResponseEntity<>(new RestResponse(false, "CONFIRMED AND PASSWORD UPDATED"), HttpStatus.OK);
        } catch (Exception e) {
           // log.error("Error while Confirming Client and Updating Client password", e);
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
