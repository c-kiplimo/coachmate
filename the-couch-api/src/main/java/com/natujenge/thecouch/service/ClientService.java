package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
    public Optional<Client> updateClient(Client client) {
        // TODO: To update client, Get client from dB, then update the details from request
        //  and then save. @Transactional
        Optional<Client> clientOptional = clientRepository.findClientById(client.getId());

        log.info("Client found with id {}", client.getId());

        if(clientOptional.isPresent()) {
            Client client1 = clientOptional.get();

            client1.setFirstName(client.getFirstName());
            client1.setLastName(client.getLastName());
            client1.setEmail_address(client.getEmail_address());
            client1.setMsisdn(client.getMsisdn());
            client1.setPhysical_address(client.getPhysical_address());
            client1.setProfession(client.getProfession());
            client1.setPayment_mode(client.getPayment_mode());
            client1.setReason(client.getReason());
            client1.setType(client.getType());

            client1 = clientRepository.save(client1);
            log.info("Updated Client with Id :", client1.getId());
            return Optional.of(client1);

        }
        return clientOptional;
    }
    //Update Client Status
    public Optional<Client> updateClientStatus(Client client) {
        Optional<Client> clientOptional = clientRepository.findClientById(client.getId());
        log.info("Client found with id {}", client.getId());

        if(clientOptional.isPresent()){
            Client client1 = clientOptional.get();

            client1.setStatus(client.getStatus());

            client1 = clientRepository.save(client1);
            log.info("Updated Client Status:", client1.getStatus());
            return Optional.of(client1);
        }
        return clientOptional;
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