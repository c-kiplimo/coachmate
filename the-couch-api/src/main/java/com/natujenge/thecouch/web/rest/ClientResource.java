package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.service.ClientService;
import com.natujenge.thecouch.service.UserService;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path="/api/clients")
public class ClientResource {

    @Autowired
    ClientService clientService;


    @GetMapping
    public ResponseEntity<?> getClients(){
        try{
            List<Client> clientList = clientService.viewClients();

            return new ResponseEntity(clientList, HttpStatus.OK);
        } catch(Exception e) {
            log.error(".....", e);
            return new ResponseEntity( "Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody Client client){
        try{
            log.info("Client request received {)", client);

            Client clientResponse = clientService.createClient(client);
            return new ResponseEntity(clientResponse, HttpStatus.CREATED);

        } catch(Exception e) {
            log.error(".....", e);
            return new ResponseEntity( "Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
