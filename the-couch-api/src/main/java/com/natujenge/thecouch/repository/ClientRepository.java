package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {
    List<Client> findAll();
}
