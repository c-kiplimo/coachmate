package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Client;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends PagingAndSortingRepository<Client,Long>, QuerydslPredicateExecutor<Client> {
    List<Client> findAll();
    void deleteClientById(long id);
    Optional<Client> findClientById(Long id);

    Optional<Client> findClientByIdAndCoachId(Long clientId,Long coachId);
}
