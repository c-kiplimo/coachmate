package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.enums.ClientStatus;
import com.natujenge.thecouch.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ClientService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ClientRepository clientRepository;

    public Client createClient(Client client) {
        try{
            clientRepository.save(client);
            return client;
        } catch (Exception e) {
            log.error("Error occurred ", e);
            return null;
        }
    }

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

    public List<Client> viewClients() {
        return clientRepository.findAll();
    }

    public String deleteClient(Client client){
        clientRepository.delete(client);

        return "SUCCESS";
    }
}
