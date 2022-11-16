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