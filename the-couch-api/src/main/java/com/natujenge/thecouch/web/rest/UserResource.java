package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.UserRole;
import com.natujenge.thecouch.service.RegistrationService;
import com.natujenge.thecouch.service.UserService;
import com.natujenge.thecouch.util.PaginationUtil;
import com.natujenge.thecouch.web.rest.dto.ClientDTO;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.request.ClientRequest;
import com.natujenge.thecouch.web.rest.request.RegistrationRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(path = "/api/users")
public class UserResource {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final RegistrationService registrationService;

    public UserResource(ModelMapper modelMapper, UserService userService, RegistrationService registrationService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.registrationService = registrationService;
    }


     //api to create a coach by organization
     @PostMapping(path = "/coach")
     ResponseEntity<?> addNewCoach(@RequestBody RegistrationRequest registrationRequest,
                                   @AuthenticationPrincipal User userDetails) {

         try {
             Organization organization = userDetails.getOrganization();
             log.info("request to add new coach by organization{}",organization.getId());
             registrationService.addCoach(organization, registrationRequest);

             return new ResponseEntity<>(new RestResponse(false,
                     "Coach by organization  created"), HttpStatus.OK);

         } catch (Exception e) {
             log.error("Error ", e);
             return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                     HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }

    //api to create a client user
    @PostMapping
    ResponseEntity<?> addNewClient(@RequestBody ClientRequest clientRequest,
                                   @AuthenticationPrincipal User userDetails) {
        log.info("request to add new client");
        try {
            Optional<Organization> organization = Optional.ofNullable(userDetails.getOrganization());

            User newClientUser;
            if(organization.isPresent()){
                newClientUser = userService.addNewClient(userDetails,
                        organization.get(),
                        clientRequest);
            } else {
                newClientUser = userService.addNewClient(userDetails,
                        null, clientRequest);
            }

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

    //GET CLIENTS By ORG ID or COACH ID
    @GetMapping(path = "clients")
    ResponseEntity<List<ClientDTO>> getClients(@RequestParam(name = "orgId", required = false) Long orgId,
                                               @RequestParam(name = "coachId", required = false) Long coachId,
                                               @RequestParam(name = "status", required = false) String status,
                                               @RequestParam(name = "search", required = false) String search,
                                               Pageable pageable,
                                               @AuthenticationPrincipal User userDetails){
        log.info("Request to get clients {}, " , coachId);
        Long organizationId = null;
        if (userDetails.getOrganization() != null) {
            organizationId = userDetails.getOrganization().getId();
        }
        Page<ClientDTO> clientDtoPage = userService.getClients(coachId, status, search, organizationId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), clientDtoPage);
        return ResponseEntity.ok().headers(headers).body(clientDtoPage.getContent());
    }

    //GET CLIENT BY ID
    @GetMapping(path = "client/{id}")
    ResponseEntity<?> getClientById(@PathVariable("id") Long clientId,
                                     @AuthenticationPrincipal User userDetails){
        log.info("Request to get client {}", clientId);
        try{
            User client = userService.getClientById(clientId, userDetails);
            if(client != null){
                ClientDTO response = modelMapper.map(client, ClientDTO.class);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new RestResponse(true,
                        "Client not found"), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //EDIT CLIENT
    @PutMapping(path = "client/{id}")
    ResponseEntity<?> editClient(@PathVariable("id") Long clientId,
                                 @RequestBody ClientRequest clientRequest,
                                 @AuthenticationPrincipal User userDetails){
        log.info("Request to edit client {}", clientId);
        try{
            Optional<Organization> organization = Optional.ofNullable(userDetails.getOrganization());
            User editedClient;
            if(organization.isPresent()){
                editedClient = userService.editClient(clientId, userDetails, clientRequest);
            } else {
                editedClient = userService.editClient(clientId, userDetails, clientRequest);
            }

            if(editedClient != null){
                ClientRequest response = modelMapper.map(editedClient, ClientRequest.class);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new RestResponse(true,
                        "Client not edited"), HttpStatus.OK);
            }
        } catch (Exception e) {
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
