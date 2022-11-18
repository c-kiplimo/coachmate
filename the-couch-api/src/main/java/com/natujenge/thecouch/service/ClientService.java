package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ClientService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ClientRepository clientRepository;

    //CREATE
    public Client createClient(Client client) {
        // TODO: Link this to user obext, and assign a default password, set status to new, have satus change to ACTIVE
        //  after client activates account.

        // TODO: Have contracts generated and assigned to client
        //  Client to review contract and update accordingly as defined in sessions repo

        try{
            clientRepository.save(client);
            return client;
        } catch (Exception e) {
            log.error("Error occurred ", e);
            return null;
        }
    }

    //UPDATE
    public Client updateClient(Client client) {
        // TODO: To update client, Get client from dB, then update the details from request
        //  and then save. @Transactional
        try {
            log.info("Received an update request for {}", client.getName());
            clientRepository.save(client);
            return client;
        } catch (Exception e) {
            log.error("Error occurred", e);
            return null;
        }
    }
    //INDEX - all clients
    public List<Client> viewClients() {
        // TODO: Return a pageable object
        return clientRepository.findAll();
    }

    //SHOW - one client
    public Client findClientById(long id) {
        return clientRepository.findClientById(id)
                .orElseThrow(() -> new UserNotFoundException("Client by id " + id + " not found"));
    }

    //DELETE a client
    public void deleteClient(Long id){
        clientRepository.deleteClientById(id);
    }
}