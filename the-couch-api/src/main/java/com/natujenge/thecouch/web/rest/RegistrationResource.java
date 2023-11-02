package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Response;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.service.*;
import com.natujenge.thecouch.web.rest.dto.InquiryDTO;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.request.CoachRequest;
import com.natujenge.thecouch.web.rest.request.ForgotPassword;
import com.natujenge.thecouch.web.rest.request.RegistrationRequest;
import com.natujenge.thecouch.web.rest.request.UserTokenConfirmRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
   // private final ClientService clientService;
    private final CoachService coachService;
    private final ResponseService responseService;
    private final UserService userService;

    @Autowired
    InquiryService inquiryService;


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

    @PostMapping(path = "/confirmCoachToken")
    ResponseEntity<?> updateAndConfirmCoachToken(@RequestBody UserTokenConfirmRequest userTokenConfirmRequest){
        log.info("Request to confirm coach token and update password");
        try {
            Optional<User> updateCoach = userService.confirmCoachTokenAndUpdatePassword(userTokenConfirmRequest);

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
    ResponseEntity<?> saveAndSendInquiry(@RequestBody InquiryDTO inquiryDTO){
        log.info("REST Request to save and send  inquiry: {}", inquiryDTO);

        try {
            inquiryService.saveAndSendEmail(inquiryDTO);
            return new ResponseEntity<>(new RestResponse(false,"Response Received Successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(path = "suport")
    ResponseEntity<?> suport(@RequestBody InquiryDTO inquiryDTO,
                             @AuthenticationPrincipal User user){
        log.info("REST Request to save and send  inquiry: {}", inquiryDTO);

        try {
            inquiryService.support(inquiryDTO,user);
            return new ResponseEntity<>(new RestResponse(false,"Response Received Successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
