package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.web.rest.dto.ClientDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface ClientRepository extends PagingAndSortingRepository<User,Long>, QuerydslPredicateExecutor<User> {
//    List<User> findByFullNameContainingOrIdContaining(@Param("text") String text, @Param("text1") Long text1);
//    List<User> findByFullNameContaining(@Param("text")String text);
//    Page<ClientDto> findAllByCoach_id(long bakerId, Pageable pageable);
//    Page<ClientDto> findByCoachIdAndFullNameContaining(Long coachId, String name, Pageable pageable);
//    Page<ClientDto> findByCoachIdAndId(Long bakerId, Long id, Pageable pageable);
//
//    // Single customer by id and coachId
//    Optional<ClientDto> findByCoachIdAndId(Long coachId, Long id);
//    Optional<User> findByIdAndCoachId(Long id, Long coachId);
//    Optional<User> findByIdAndOrganizationId(Long id, Long orgId);
//    boolean existsByIdAndCoachId(Long id,Long coachId);
//
//    Optional<User> findClientByIdAndCoachId(Long clientId, Long coachId);
//
//    List<User> findByEmail(String email);
//
//    List<User> findClientByOrganization(Organization orgId);
//
//    Optional<User> findClientByEmail(String emailAddress);

    List<User> getByOrganizationId(Long orgId);
}
