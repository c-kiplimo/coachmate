package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.web.rest.dto.ClientDto;
import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Long> {
    List<Client> findAll();
    void deleteClientById(long id);
    Optional<Client> findClientById(Long id);
}
