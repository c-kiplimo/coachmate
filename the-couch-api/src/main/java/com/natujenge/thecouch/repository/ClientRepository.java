package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.web.rest.dto.ClientDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface ClientRepository extends PagingAndSortingRepository<Client,Long>, QuerydslPredicateExecutor<Client> {
    List<Client> findByFullNameContainingOrIdContaining(@Param("text") String text, @Param("text1") Long text1);
    List<Client> findByFullNameContaining(@Param("text")String text);
    Page<ClientDto> findAllByCoach_id(long bakerId, Pageable pageable);
    Page<ClientDto> findByCoachIdAndFullNameContaining(Long coachId, String name, Pageable pageable);
    Page<ClientDto> findByCoachIdAndId(Long bakerId, Long id, Pageable pageable);

    // Single customer by id and coachId
    Optional<ClientDto> findByCoachIdAndId(Long coachId, Long id);
    Optional<Client> findByIdAndCoachId(Long id, Long coachId);
    boolean existsByIdAndCoachId(Long id,Long coachId);

    Optional<Client> findClientByIdAndCoachId(Long clientId, Long coachId);

    List<Client> findByEmail(String email);

    List<Client> findClientByOrganization(Organization orgId);

    Optional<Client> findClientByEmail(String emailAddress);

    List<Client> getByOrganizationId(Long orgId);
}
