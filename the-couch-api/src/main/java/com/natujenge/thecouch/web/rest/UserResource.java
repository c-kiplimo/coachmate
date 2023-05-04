package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.UserRole;
import com.natujenge.thecouch.service.UserService;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.request.ClientRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(path = "/api/users")
public class UserResource {
    private final ModelMapper modelMapper;
    private final UserService userService;

    public UserResource(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    //api to create a client user
    @PostMapping
    ResponseEntity<?> addNewClient(@RequestBody ClientRequest clientRequest,
                                   @AuthenticationPrincipal User userDetails) {
        log.info("request to add new client");
        try {
            Optional<Organization> organization = Optional.ofNullable(userDetails.getOrganization());

            User newClientUser = userService.addNewClient(userDetails,organization,
                    clientRequest, userDetails.getMsisdn());

            if (newClientUser != null) {
                ClientRequest response = modelMapper.map(newClientUser, ClientRequest.class);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new RestResponse(true,
                        "Client not created"), HttpStatus.OK);
            }
        }  catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //API TO GET CLIENTS BY ORG ID
    @GetMapping(path = "getOrgClients/{id}")
    ResponseEntity<?> getOrgClients(@PathVariable("id") Long OrgId,
                                    @AuthenticationPrincipal User userDetails){
        log.info("Request to get Organization clients");
        try{
            List<User> listResponse = userService.getClientByOrgId(userDetails.getOrganization().getId(), UserRole.CLIENT);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error Occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "Error Occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
