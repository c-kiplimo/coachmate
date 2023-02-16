package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.service.RegistrationService;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.request.ForgotPassword;
import com.natujenge.thecouch.web.rest.request.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/registration")
@AllArgsConstructor
public class RegistrationResource {

    private final RegistrationService registrationService;

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



}
