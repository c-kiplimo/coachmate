package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.web.rest.dto.ClientDto;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ClientRepository extends PagingAndSortingRepository<Client,Long>, QuerydslPredicateExecutor<Client> {
    List<Client> findAll();
    void deleteClientById(long id);
    Optional<Client> findClientById(Long id);

    Optional<Client> findClientByIdAndCoachId(Long clientId,Long coachId);
}
