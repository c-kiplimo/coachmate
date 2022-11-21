package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.domain.enums.SessionStatus;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.SessionRepository;
import com.natujenge.thecouch.service.SessionService;
import com.natujenge.thecouch.web.rest.dto.SessionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.StringUtils.hasLength;

@RestController
@Slf4j
@RequestMapping(name = "/api/sessions")
public class SessionResource {

    @Autowired
    SessionService sessionService;
    @Autowired
    SessionRepository sessionRepository;

    //GET: /sessions
    @GetMapping
    public ResponseEntity<?> getSessions(){
        //TODO:  Exception Handling here. Custom messages, and Logging for all actions
        //
        try{
            List<Session> sessionList = sessionService.viewSessions();

            return new ResponseEntity<>(sessionList, HttpStatus.OK);
        } catch(Exception e) {
            log.error(".....", e);
            return new ResponseEntity<>( "Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET: /api/sessions/:id
    @GetMapping("/{id}")
    public ResponseEntity<?> getSessionById (@PathVariable("id") Long id) {
        Session session = sessionService.findSessionById(id);
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    //POST: /api/sessions
    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody Session session){
        try{
            log.info("Session request received {}", session);

            Session sessionResponse = sessionService.createSession(session);
            return new ResponseEntity<>(sessionResponse, HttpStatus.CREATED);

        } catch(Exception e) {
            log.error(".....", e);
            return new ResponseEntity<>( "Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //PUT: /api/sessions
    @PutMapping
    public ResponseEntity<?> updateSession(@RequestBody Session session){
        try{
            log.info("Session request received {}", session);
            Session sessionResponse = sessionService.updateSession(session);
            return new ResponseEntity<>(sessionResponse, HttpStatus.OK);

        } catch(Exception e) {
            log.error(".....", e);
            return new ResponseEntity<>( "Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //PATCH: /api/sessions/:id
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateSessionById(@RequestBody SessionDto sessionDto, @PathVariable("id") Long id) {
        Session session = sessionRepository.findSessionById(id).orElseThrow(() -> new UserNotFoundException("Session by id " + id + " not found"));

        boolean needUpdate = false;

        if (hasLength(sessionDto.getName())){
            session.setName(sessionDto.getName());
            needUpdate = true;
        }

        if (hasLength(sessionDto.getDetails())){
            session.setDetails(sessionDto.getDetails());
            needUpdate = true;
        }

        if (hasLength(sessionDto.getAmount_paid())){
            session.setAmount_paid(sessionDto.getAmount_paid());
            needUpdate = true;
        }

        if (hasLength(sessionDto.getStatus())){
            session.setStatus(SessionStatus.valueOf(sessionDto.getStatus()));
            needUpdate = true;
        }

        if (hasLength(sessionDto.getSession_venue())){
            session.setSession_venue(sessionDto.getSession_venue());
            needUpdate = true;
        }

        if (hasLength(sessionDto.getAttachments())){
            session.setAttachments(sessionDto.getAttachments());
            needUpdate = true;
        }


        if (needUpdate) {
            sessionRepository.save(session);
        }
        return new ResponseEntity(session, HttpStatus.OK);
    }

    //DELETE:/api/sessions/:id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable("id") Long id) {
        sessionService.deleteSession(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
