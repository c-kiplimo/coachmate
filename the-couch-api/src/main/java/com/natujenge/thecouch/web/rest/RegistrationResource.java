package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.service.RegistrationService;
import com.natujenge.thecouch.web.rest.request.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/registration")
@AllArgsConstructor
public class RegistrationResource {

    private final RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest registrationRequest){
        return registrationService.register(registrationRequest);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }


}
