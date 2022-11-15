package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(path="/api/clients")
public class ClientResource {

    @Autowired
    ClientService clientService;
    
    public ResponseEntity<?> getClients(){
        try{

        } catch(Exception e) {
            log.error(".....", e);
            return new ResponseEntity( "Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
