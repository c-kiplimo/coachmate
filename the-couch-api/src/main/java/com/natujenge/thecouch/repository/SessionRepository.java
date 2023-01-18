package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.web.rest.dto.SessionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface SessionRepository extends PagingAndSortingRepository<Session,Long>, QuerydslPredicateExecutor<Session> {
    List<Session> findAll();
    void deleteSessionById(long id);
    Optional<Session> findSessionById(Long id);


    Page<SessionDto> findAllByCoach_id(Long id, Pageable pageable);

    Optional<SessionDto> findByIdAndCoachId(Long id, Long coachId);

    Optional<Session> findSessionByIdAndCoachId(Long sessionId, Long coachId);

    boolean existsByIdAndCoachId(Long id, Long coachId);

    Optional<Session> getSessionByIdAndCoachId(Long sessionId, Long coachId);

}
