package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.SessionStatus;
import com.natujenge.thecouch.repository.*;
import com.natujenge.thecouch.service.notification.NotificationServiceHTTPClient;
import com.natujenge.thecouch.util.NotificationHelper;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.dto.SessionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.natujenge.thecouch.domain.Session;
import javax.transaction.Transactional;
import java.time.LocalDate;
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
    OrganizationRepository organizationRepository;
    @Autowired
    private NotificationServiceHTTPClient notificationServiceHTTPClient;

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

    // Get Individual Sessions by id
    public Session findSessionByIdAndCoachId(Long id) {
        log.debug("Request to get session : {} and coachId : {}", id);

        Optional<Session> sessionOptional = sessionRepository.findById(id);
        if (sessionOptional.isPresent()) {
            return sessionOptional.get();

        } else {
            throw new IllegalArgumentException("Session not found!");

        }
    }


    //CREATE NEW CONTRACT
    public Session createSession(Long coachId,Long clientId,Long contractId, Session sessionRequest) throws IllegalArgumentException {
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
        Optional<Organization> optionalOrganization = organizationRepository.findBySuperCoachId(coachId);

        // Client
        Client client = optionalClient.get();
        // Coach
        Coach coach = client.getCoach();
        // Contract
        Contract contract = optionalContract.get();

        // New Sessions enter the confirmed state
        sessionRequest.setSessionStatus(SessionStatus.CONFIRMED);
        sessionRequest.setCoach(coach);
        sessionRequest.setClient(client);
        sessionRequest.setContract(contract);
        sessionRequest.setCreatedBy(coach.getFullName());
        sessionRequest.setLastUpdatedBy(coach.getFullName());
        if(optionalOrganization.isPresent()){
            sessionRequest.setOrgId(optionalOrganization.get().getId());
        }
        //set organization id null if not found
        else{
            sessionRequest.setOrgId(null);
        }

        try{
            return sessionRepository.save(sessionRequest);
        } catch (Exception e) {
            log.error("Error occurred ", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    //UPDATE SESSION
    public Optional<Session> updateSession(Long sessionId, Long coachId,Session sessionRequest){

        Optional<Session> sessionOptional = sessionRepository.findSessionByIdAndCoachId(sessionId,coachId);

        if (sessionOptional.isPresent()) {
            log.info("Session with id {} found", sessionId);
            Session session = sessionOptional.get();

            if (sessionRequest.getName() != null && sessionRequest.getName().length() > 0) {
                session.setName(sessionRequest.getName());
            }

            if (sessionRequest.getSessionType() != null) {
                session.setSessionType(sessionRequest.getSessionType());
            }

            if (sessionRequest.getSessionStatus() != null) {
                session.setSessionStatus(sessionRequest.getSessionStatus());
            }

            if (sessionRequest.getNotes() != null && session.getNotes().length() > 0) {
                session.setNotes(sessionRequest.getNotes());
            }

            if (sessionRequest.getSessionDate() != null) {
                session.setSessionDate(sessionRequest.getSessionDate());
            }
            if (sessionRequest.getSessionDuration() != null && session.getSessionDuration().length() > 0) {
                session.setSessionDuration(sessionRequest.getSessionDuration());
            }
            if (sessionRequest.getSessionStartTime() != null) {
                session.setSessionStartTime(sessionRequest.getSessionStartTime());
            }
            if (sessionRequest.getSessionEndTime() != null) {
                session.setSessionEndTime(sessionRequest.getSessionEndTime());
            }
            if (sessionRequest.getSessionVenue() != null) {
                session.setSessionVenue(sessionRequest.getSessionVenue());
            }
            if (sessionRequest.getPaymentCurrency() != null) {
                session.setPaymentCurrency(sessionRequest.getPaymentCurrency());
            }
            if (sessionRequest.getAmountPaid() != null) {
                session.setAmountPaid(sessionRequest.getAmountPaid());
            }
            sessionRepository.save(session);
            return Optional.of(session);

        }
        return sessionOptional;
    }

    // Delete session by ID
    public void deleteSession(Long id,Long coachId) {
        log.debug("Request to delete session : {}", id);


        boolean exist = sessionRepository.existsByIdAndCoachId(id,coachId);
        if (!exist) {
            throw new IllegalStateException("session doesn't exist");

        }
        coachRepository.deleteById(id);
    }

    public List<SessionDto> findSessionByClientId(Long clientId) {
        log.debug("Request to get sessions  by clientId : {}", clientId);
        return sessionRepository.findByClientId(clientId);
    }

    public List<SessionDto> findSessionByContractId(Long contractId) {
        log.debug("Request to get sessions by contractId : {}", contractId);
        return sessionRepository.findByContractId(contractId);
    }


    //send notification to client and coach when session is upcoming
    @Scheduled(cron = "0 0 9 * * *")
    public void sendUpcomingSessionReminderToCoach() {
        log.debug("Request to send upcoming session reminder");
        List<Session> sessions = sessionRepository.findSessionBySessionDate(LocalDate.now());

        for (Session session : sessions) {
            String smsContent;
            smsContent = "Hello " + session.getCoach().getFirstName()+",\n You have an upcoming session " + session.getName()+" with " +
                    " client: " + session.getClient().getFullName() + "\n The session will be " + session.getSessionVenue()+ " at "
                    + session.getSessionStartTime() + " to " + session.getSessionEndTime() + "\n See you there!";
            String smsContentClient;
            smsContentClient = "Hello " + session.getClient().getFirstName()+",\n You have an upcoming session" + session.getName()+"with " +
                    " coach: " + session.getCoach().getFullName() + "\n The session will be " + session.getSessionVenue()+ " at "
                    + session.getSessionStartTime() + "to " + session.getSessionEndTime() + "\n See you there!";
                //send notification to coach
                NotificationHelper.sendUpcomingSessionReminderToCoach(session);
                // sendEmail
              String email =  session.getCoach().getEmailAddress();
                notificationServiceHTTPClient.sendEmail(email,"SESSION DUE TODAY",smsContent,false);
                log.info("Email sent");
                //send notification to client
                NotificationHelper.sendUpcomingSessionReminderToClient(session);
                // sendEmail
                String clientEmail =  session.getClient().getEmail();
                notificationServiceHTTPClient.sendEmail(clientEmail,"SESSION DUE TODAY", smsContentClient,false);
            }
        }

//    @Scheduled(cron = "0 20 05 * * ?")
//    public void sendUpcomingSessionReminderToClient() {
//        log.debug("Request to send upcoming session reminder");
//        List<Session> sessions = sessionRepository.findSessionBySessionDate(LocalDate.now());
//        for (Session session : sessions) {
//            if (session.getSessionStatus().equals(SessionStatus.CONFIRMED)) {
//                //send notification to client
//                NotificationHelper.sendUpcomingSessionReminderToClient(session);
//                //send notification to client
//                NotificationHelper.sendUpcomingSessionReminderToClient(session);
//            }
//        }
//    }




    public List<Session> getSessionByOrgId(Long orgId) {
        return sessionRepository.findSessionByOrgId(orgId);
    }

    public List<Session> getSessionsByContract(Long contractId) {
        return sessionRepository.findSessionByContractId(contractId);
    }
}
