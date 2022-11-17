package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@Transactional

public class SessionService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SessionRepository sessionRepository;

    //CREATE
    public Session createSession(Session session) {
        try{
            sessionRepository.save(session);
            return session;
        } catch (Exception e) {
            log.error("Error occurred ", e);
            return null;
        }
    }

    //UPDATE
    public Session updateSession(Session session) {
        try {
            log.info("Received an update request for {}", session.getName());
            sessionRepository.save(session);
            return session;
        } catch (Exception e) {
            log.error("Error occurred", e);
            return null;
        }
    }
    //INDEX - all session
    public List<Session> viewSessions() {
        return sessionRepository.findAll();
    }

    //SHOW - one session
    public Session findSessionById(long id) {
        return sessionRepository.findSessionById(id)
                .orElseThrow(() -> new UserNotFoundException("Session by id " + id + " not found"));
    }

    //DELETE a session
    public void deleteSession(Long id){
        sessionRepository.deleteSessionById(id);
    }


}
