package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.web.rest.dto.SessionDto;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface SessionRepository extends PagingAndSortingRepository<Session,Long>, QuerydslPredicateExecutor<Session> {
    List<Session> findAll();
    void deleteSessionById(long id);
    Optional<Session> findSessionById(Long id);


    Page<SessionDto> findAllByCoach_id(Long id, Pageable pageable);
    Optional<Session> findByIdAndCoach_id(Long id, Long coachId);

    Optional<Session> findSessionByIdAndCoachId(Long sessionId, Long coachId);

    boolean existsByIdAndCoachId(Long id, Long coachId);

    List<SessionDto> findByClientId(Long clientId);

    List<SessionDto> findByContractId(Long contractId);

    List<Session> findSessionByOrgId(Long orgId);

    Optional<Session> getSessionById(Long id);

    List<Session> findSessionByContractId(Long contractId);

    List<Session> findAllBysessionSchedules(LocalDate date);

    Optional<SessionDto> findByIdAndCoachId(Long sessionId, Long coachId);

    Page<SessionDto> findAll(Example<Session> example, Pageable pageable);

    Page<SessionDto> findAllByCoachId(Long id, Pageable pageable);

}
