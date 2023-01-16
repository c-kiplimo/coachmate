package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.ClientRepository;
import com.natujenge.thecouch.repository.CoachRepository;
import com.natujenge.thecouch.repository.ContractRepository;
import com.natujenge.thecouch.repository.SessionRepository;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.dto.SessionDto;
import com.natujenge.thecouch.web.rest.request.SessionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional

public class SessionService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ContractRepository contractRepository;

    @Autowired
    CoachRepository coachRepository;

    // GetAllSessions
    public ListResponse getAllSessions(int page, int perPage, String search, Long id) {
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<SessionDto> sessionPage;
        if (search != null && !search.isEmpty()) {
            QSession qSession = QSession.session;
            sessionPage = sessionRepository.findBy(qSession.coach.id.eq(id).
                            andAnyOf(qSession.name.containsIgnoreCase(search).
                                    or(qSession.notes.containsIgnoreCase(search))),
                    q -> q.sortBy(sort).as(SessionDto.class).page(pageable));
        } else {
            sessionPage = sessionRepository.findAllByCoach_id(id, pageable);
        }

        return new ListResponse(sessionPage.getContent(), sessionPage.getTotalPages(), sessionPage.getNumberOfElements(),
                sessionPage.getTotalElements());

    }

    // Get Individual Sessions by Id
    public SessionDto findSessionByIdAndCoachId(Long id, Long coachId) {
        log.debug("Request to get session : {} and coachId : {}", id,coachId);

        Optional<SessionDto> sessionOptional = sessionRepository.findByIdAndCoachId(id,coachId);
        if (sessionOptional.isPresent()) {
            return sessionOptional.get();

        } else {
            throw new IllegalArgumentException("Session not found!");

        }
    }


    //CREATE NEW CONTRACT
    public Session createSession(Long coachId,Long clientId,Long contractId, Session sessionRequest) {
        log.info("Creating new session");
        Optional<Client> optionalClient = clientRepository.findClientByIdAndCoachId(clientId,coachId);
        Optional<Contract> optionalContract = contractRepository.findByIdAndCoachId(contractId,coachId);

        if(optionalClient.isEmpty()){
            log.warn("Client with id {} not found", clientId);
            throw new IllegalArgumentException("Client not found!");
        }

        if(optionalContract.isEmpty()){
            log.warn("Contract with id {} not found", contractId);
            throw new IllegalArgumentException("Contract not found!");
        }
        // Client
        Client client = optionalClient.get();
        // Coach
        Coach coach = client.getCoach();
        // Contract
        Contract contract = optionalContract.get();

        sessionRequest.setCoach(coach);
        sessionRequest.setClient(client);
        sessionRequest.setContract(contract);
        sessionRequest.setCreatedBy(coach.getFullName());
        sessionRequest.setLastUpdatedBy(coach.getFullName());

        try{
            return sessionRepository.save(sessionRequest);
        } catch (Exception e) {
            log.error("Error occurred ", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    //UPDATE CONTRACT
    public Session updateSession(Session session) {
        // TODO: Update details rather than save new Entry
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
        //TODO: return pageable, sessionDto
        return sessionRepository.findAll();
    }

    //DELETE a session
    public void deleteSession(Long id){
        sessionRepository.deleteSessionById(id);
    }

}
