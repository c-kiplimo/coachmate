package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {
    List<Session> findAll();
    void deleteSessionById(long id);
    Optional<Session> findSessionById(Long id);

    void findSessionByCoachId(Long coachId);
}
