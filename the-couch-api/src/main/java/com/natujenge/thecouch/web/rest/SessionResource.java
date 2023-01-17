package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.SessionStatus;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.SessionRepository;
import com.natujenge.thecouch.service.SessionService;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.dto.SessionDto;
import com.natujenge.thecouch.web.rest.request.SessionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasLength;

@RestController
@Slf4j
@RequestMapping(path = "/api/sessions")
public class SessionResource {

    @Autowired
    SessionService sessionService;
    @Autowired
    SessionRepository sessionRepository;

    //GET: /sessions
    @GetMapping
    public ResponseEntity<?> getSessions(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @RequestParam(name = "search", required = false) String search,
            @AuthenticationPrincipal User userDetails
    ){
        try {
            ListResponse listResponse = sessionService.getAllSessions(page, perPage, search, userDetails.getCoach().getId());
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET: Get individual sessions by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getSessionById (@PathVariable("id") Long id,
                                             @AuthenticationPrincipal User userDetails) {

        try {
            SessionDto sessionRequest = sessionService.findSessionByIdAndCoachId(id, userDetails.getCoach().getId());
            return new ResponseEntity<>(sessionRequest, HttpStatus.OK);

        } catch (Exception e) {
            log.error("error ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //POST: /api/sessions
    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody Session session,
                                           @RequestParam("contractId") Long contractId,
                                           @RequestParam("clientId") Long clientId,
                                           @AuthenticationPrincipal User userDetails){
        log.info("Request to create session");
        try{
            Session sessionResponse = sessionService.createSession(userDetails.getCoach().getId(),clientId,contractId,session);
            return new ResponseEntity<>(new RestResponse(false,"Session Created Successfully"), HttpStatus.CREATED);

        } catch(Exception e) {
            log.error("Error", e);
            return new ResponseEntity<>( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //PUT: /api/sessions
    // UPDATE SESSION DETAILS
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateSession(@RequestBody Session session,
                                           @PathVariable("id") Long id,
                                           @AuthenticationPrincipal User userDetails){
        Coach coach = userDetails.getCoach();
        try{
            log.info("Update Session request received");
            Optional<Session> updatedSession = sessionService.updateSession(id,coach.getId(),session);
            if (updatedSession.isEmpty()){
                throw new NoSuchElementException();
            }
            return new ResponseEntity<>(updatedSession.get(), HttpStatus.OK);

        } catch(Exception e) {
            log.error(".....", e);
            return new ResponseEntity<>( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //PATCH: /api/sessions/:id
//    @PatchMapping("/{id}")
//    public ResponseEntity<?> updateSessionById(@RequestBody SessionRequest sessionRequest,
//                                               @PathVariable("id") Long id) {
//        Session session = sessionRepository.findSessionById(id).orElseThrow(()
//                -> new UserNotFoundException("Session by id " + id + " not found"));
//
//        boolean needUpdate = false;
//
//        if (hasLength(sessionRequest.getName())){
//            session.setName(sessionRequest.getName());
//            needUpdate = true;
//        }
//
//        if (hasLength(sessionRequest.getSessionDuration())){
//            session.setSessionDuration(sessionRequest.getSessionDuration());
//            needUpdate = true;
//        }
//
//        if (hasLength(sessionRequest.getAmountPaid())){
//            session.setAmountPaid(sessionRequest.getAmountPaid());
//            needUpdate = true;
//        }
//
//        if (hasLength(sessionRequest.getSessionDuration())){
//            session.setSessionDuration(sessionRequest.getSessionDuration());
//            needUpdate = true;
//        }
//
//        if (needUpdate) {
//            sessionRepository.save(session);
//        }
//        return new ResponseEntity(session, HttpStatus.OK);
//    }

    //DELETE:/api/sessions/:id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable("id") Long id,
                                          @AuthenticationPrincipal User userDetails) {
        sessionService.deleteSession(id,userDetails.getCoach().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}