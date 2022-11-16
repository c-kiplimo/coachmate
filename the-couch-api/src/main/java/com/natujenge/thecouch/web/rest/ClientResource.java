package com.natujenge.thecouch.web.rest;

import antlr.StringUtils;
import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.enums.ClientStatus;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.ClientRepository;
import com.natujenge.thecouch.service.ClientService;
import com.natujenge.thecouch.service.UserService;
import com.natujenge.thecouch.web.rest.dto.ClientDto;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.util.StringUtils.hasLength;


@RestController
@Slf4j
@RequestMapping(path="/api/clients")

public class ClientResource {

    @Autowired
    ClientService clientService;

    @Autowired
    ClientRepository clientRepository;

    //GET: /clients
    @GetMapping
    public ResponseEntity<List<Client>> getClients(){
        try{
            List<Client> clientList = clientService.viewClients();

            return new ResponseEntity(clientList, HttpStatus.OK);
        } catch(Exception e) {
            log.error(".....", e);
            return new ResponseEntity( "Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET: /api/clients/:id
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById (@PathVariable("id") Long id) {
        Client client = clientService.findClientById(id);
        return new ResponseEntity(client, HttpStatus.OK);
    }

    //POST: /api/clients
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client){
        try{
            log.info("Client request received {)", client);

            Client clientResponse = clientService.createClient(client);
            return new ResponseEntity(clientResponse, HttpStatus.CREATED);

        } catch(Exception e) {
            log.error(".....", e);
            return new ResponseEntity( "Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //PUT: /api/clients
    @PutMapping
    public ResponseEntity<Client> updateClient(@RequestBody Client client){
        try{
            log.info("Client request received {)", client);
            Client clientResponse = clientService.updateClient(client);
            return new ResponseEntity(clientResponse, HttpStatus.OK);

        } catch(Exception e) {
            log.error(".....", e);
            return new ResponseEntity( "Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //PATCH: /api/clients/:id
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateClientById(@RequestBody ClientDto clientDto, @PathVariable("id") Long id) {
        Client client = clientRepository.findClientById(id).orElseThrow(() -> new UserNotFoundException("Client by id " + id + " not found"));

        boolean needUpdate = false;

        if (hasLength(clientDto.getName())){
            client.setName(clientDto.getName());
            needUpdate = true;
        }

        if (hasLength(clientDto.getEmail_address())){
            client.setEmail_address(clientDto.getEmail_address());
            needUpdate = true;
        }

        if (hasLength(clientDto.getProfession())){
            client.setProfession(clientDto.getProfession());
            needUpdate = true;
        }

        if (hasLength(clientDto.getMsisdn())){
            client.setMsisdn(clientDto.getMsisdn());
            needUpdate = true;
        }

        if (hasLength(clientDto.getStatus())){
            client.setStatus(ClientStatus.valueOf(clientDto.getStatus()));
            needUpdate = true;
        }

        if (needUpdate) {
            clientRepository.save(client);
        }
        return new ResponseEntity(client, HttpStatus.OK);
    }

    //DELETE:/api/clients/:id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable("id") Long id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
