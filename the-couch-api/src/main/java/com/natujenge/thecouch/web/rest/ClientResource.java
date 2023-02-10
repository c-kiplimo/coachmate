package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.ClientStatus;
import com.natujenge.thecouch.service.ClientService;

import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.request.ChangeStatusRequest;
import com.natujenge.thecouch.web.rest.request.ClientRequest;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasLength;


@RestController
@Slf4j
@RequestMapping(path="/api/clients")

public class ClientResource {

    @Autowired
    private ModelMapper modelMapper;
    private final ClientService clientService;

    public ClientResource(ClientService clientService) {
        this.clientService = clientService;
    }

    //api to get all the clients
    @GetMapping
    ResponseEntity<?> getAll(@RequestParam("per_page") int perPage,
                             @RequestParam("page") int page,
                             @RequestParam(name = "status", required = false) ClientStatus clientStatus,
                             @RequestParam(name="search",required = false) String search,
                             @AuthenticationPrincipal User userDetails) {
        try {
            Long coachId = userDetails.getCoach().getId();
            log.info("Request to get all clients of Coach with Id {}",coachId);
            ListResponse listResponse = clientService.getAllClients(
                    page, perPage, search,clientStatus, coachId);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity<>(new RestResponse(true, "Error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //api to create a client
    @PostMapping
    ResponseEntity<?> addNewClient(@RequestBody ClientRequest clientRequest,
                                     @AuthenticationPrincipal User userDetails) {
        log.info("request to add new client");
        try {
            Client newClient = clientService.addNewClient(clientRequest.getCoachId(),
                    clientRequest);
            if (newClient != null) {
                ClientRequest response = modelMapper.map(newClient, ClientRequest.class);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new RestResponse(true,
                        "Client not created"), HttpStatus.OK);
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
            List<Client> listResponse = clientService.getClientByOrgId(OrgId);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error Occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "Error Occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //api to find one Client by id
    @GetMapping(path = "{id}")
    ResponseEntity<?> findById(@PathVariable("id") Long id,
                               @AuthenticationPrincipal User userDetails) {
        try{
            Optional<Client> clientOptional = clientService.findById(id);
            if (clientOptional.isPresent()) {
                return new ResponseEntity<>(clientOptional.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new RestResponse(true, "Client not found"),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true,
                    "Client could not be fetched, contact admin"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //api to update client
    @PutMapping(path = "{id}")
    ResponseEntity<?> updateClient(@RequestBody ClientRequest clientRequest,
                                     @PathVariable Long id,
                                     @AuthenticationPrincipal User userDetails){
        try{
            log.info("request to update client with id : {} by client of id {}", id,userDetails.getCoach().getId());

            clientService.updateClient(id,userDetails.getCoach().getId(), clientRequest);

            return new ResponseEntity<>(new RestResponse(false, "Client updated successfully"),
                    HttpStatus.OK);
        } catch (Exception e){
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/change-status/{id}") // change status active or suspend
    ResponseEntity<?> updateClientStatus(@RequestBody ChangeStatusRequest statusRequest,
                                     @RequestParam("status") String clientStatus,
                                     @PathVariable Long id,
                                     @AuthenticationPrincipal User userDetails) {
        try{
            log.info("request to change client status with id : {} to status {} by coach with id {}", id, clientStatus,
                    userDetails.getCoach().getId());
            clientService.updateClientStatus(id,userDetails.getCoach().getId(), clientStatus,statusRequest);

            return new ResponseEntity<>(new RestResponse(false, "Client status set to "+ clientStatus),
                    HttpStatus.OK);
        } catch (Exception e){
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping(path = "/close-client/{id}") // change status active or suspend
    ResponseEntity<?> closeClient(@RequestBody ChangeStatusRequest statusRequest,
                                    @PathVariable Long id,
                                    @AuthenticationPrincipal User userDetails) {
        try{
            log.info("request to close client with id : {} by coach with id {} ", id,userDetails.getCoach().getId());

            clientService.closeClient(id,userDetails.getCoach().getId(),statusRequest);
            return ResponseEntity.ok().body(new RestResponse(false,
                    "Client closed successfully"));

        } catch (Exception e){
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter-by-client-name")
    public ResponseEntity<?> filterByClientNameAndCoachId(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails, //later we will use security credential
            @RequestParam(name = "name",required = false) String name
    ) {
        try {
            log.debug(
                    "REST request to filter clients given, coach id : {}, client name  : {}",
                    userDetails.getCoach().getId(),
                    name
            );

            ListResponse client = clientService.filterByNameAndCoachId(
                    page,
                    perPage,
                    userDetails.getCoach().getId(),
                    name
            );

            //LATER add HTTP headers
            return ResponseEntity.ok().body(client);
        } catch (Exception e){
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/filter-by-client-id")
    public ResponseEntity<?> filterByClientIdAndCoachId(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails, //later we will use security credential
            @RequestParam(name = "id",required = false) Long id
    ) {
        try {
            log.debug(
                    "REST request to filter clients given, coach id : {}, client id  : {}",
                    userDetails.getCoach().getId(),
                    id
            );

            ListResponse client = clientService.filterByIdAndCoachId(
                    page,
                    perPage,
                    userDetails.getCoach().getId(),
                    id
            );
            //LATER add HTTP headers
            return ResponseEntity.ok().body(client);
        } catch (Exception e){
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //api to confirm client and update password
    @PostMapping(path = "/confirmClientToken")
    ResponseEntity<?> updateAndConfirmClientToken(@RequestBody ClientRequest clientRequest){
        log.info("Request to confirm client token and update password");
        try {
            Optional<User> updateClient;
            updateClient = clientService.confirmClientTokenAndUpdatePassword(clientRequest);

            return new ResponseEntity<>(new RestResponse(false, "CONFIRMED AND PASSWORD UPDATED"), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error while Confirming Client and Updating Client password", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //api to delete client
    @DeleteMapping(path = "{id}")
    ResponseEntity<?> deleteClient(@PathVariable("id") Long id,
                                     @AuthenticationPrincipal User userDetails) {
        try{
            clientService.deleteClient(id,userDetails.getCoach().getId());
            return new ResponseEntity<>(new RestResponse(false, "Client Deleted Successfully"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/getClientByEmail")
    ResponseEntity<?> getClientByEmail(@RequestBody ClientRequest clientRequest,
                                       @AuthenticationPrincipal User userDetails) {
        log.info("Request to get client by email");
        try {
           List<Client> client = clientService.findByEmail(clientRequest.getEmail());
           return ResponseEntity.ok().body(client);
        } catch (Exception e) {
            log.error("Error Occurred ", e);
            return new ResponseEntity<>(new RestResponse(true,
                    "Client could not be fetched"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        }



}
