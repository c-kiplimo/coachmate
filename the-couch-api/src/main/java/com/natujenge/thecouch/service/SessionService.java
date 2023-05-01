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
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.natujenge.thecouch.domain.Session;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Objects;


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
    SessionSchedulesRepository sessionSchedulesRepository;
    @Autowired
    private NotificationServiceHTTPClient notificationServiceHTTPClient;

    @Autowired
    SessionSchedulesService sessionSchedulesService;

    @Autowired
    CoachRepository coachRepository;

    // GetAllSessions
    public ListResponse getAllSessions(int page, int perPage, String search, Long id) {
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<SessionDto> sessionPage;

            sessionPage = sessionRepository.findAllByCoachId(id, pageable);

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


    //CREATE NEW SESSION
    public Session createSession(Long coachId, Long clientId, Long contractId, Session sessionRequest) throws IllegalArgumentException {
        log.info("Creating new session");

        Optional<Client> optionalClient = clientRepository.findClientByIdAndCoachId(clientId,coachId);
        Optional<Contract> optionalContract = contractRepository.findByIdAndCoachId(contractId,coachId);
        Optional<SessionSchedules> optionalSessionSchedules = sessionSchedulesRepository.findById(sessionRequest.getSessionSchedules().getId());

        if (optionalClient.isEmpty()) {
            log.warn("Client with id {} not found", clientId);
            throw new IllegalArgumentException("Client not found!");
        }

        if (optionalContract.isEmpty()) {
            log.warn("Contract with id {} not found", contractId);
            throw new IllegalArgumentException("Contract not found!");
        }
        if(optionalSessionSchedules.isEmpty()){
            log.warn("Session slot with Id {} not found", sessionRequest.getSessionSchedules().getId());
            throw new IllegalArgumentException("Session Slot no found!");
        }
        Optional<Organization> optionalOrganization = organizationRepository.findBySuperCoachId(coachId);

        // Client
        Client client = optionalClient.get();
        // Coach
        Coach coach = client.getCoach();
        // Contract
        Contract contract = optionalContract.get();
        //Session slot
        SessionSchedules sessionSchedules = optionalSessionSchedules.get();

        // New Sessions enter the new state
        sessionRequest.setSessionStatus(SessionStatus.NEW);
        sessionRequest.setCoach(coach);
        sessionRequest.setClient(client);
        sessionRequest.setContract(contract);
        sessionRequest.setSessionSchedules(sessionSchedules);
        sessionRequest.setCreatedBy(coach.getFullName());
        sessionRequest.setLastUpdatedBy(coach.getFullName());
        // session Number Generation
        int randNo = (int) ((Math.random() * (999 - 1)) + 1);
        String sessionL = String.format("%05d", randNo);
        String sessionNo = client.getCoach().getBusinessName().substring(0, 2) +
                client.getFirstName().charAt(0) + client.getLastName().charAt(0) + "-" + sessionL;
        sessionRequest.setSessionNumber(sessionNo);
        if (optionalOrganization.isPresent()) {
            sessionRequest.setOrgId(optionalOrganization.get().getId());
        }
        //set organization id null if not found
        else {
            sessionRequest.setOrgId(null);
        }

        try {
            sessionSchedulesService.updateBookedStateToTrue(sessionSchedules.getId());
            return sessionRepository.save(sessionRequest);
        } catch (Exception e) {
            log.error("Error occurred ", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    //UPDATE SESSION
    public Optional<Session> updateSession(Long sessionId, Long coachId, Session sessionRequest) {

        Optional<Session> sessionOptional = sessionRepository.findSessionByIdAndCoachId(sessionId, coachId);

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

            if (sessionRequest.getSessionSchedules() != null) {
                session.setSessionSchedules(sessionRequest.getSessionSchedules());
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
    public void deleteSession(Long id, Long coachId) {
        log.debug("Request to delete session : {}", id);


        boolean exist = sessionRepository.existsByIdAndCoachId(id, coachId);
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
    @Scheduled(cron = "0 0 6 * * *")
    public void sendUpcomingSessionReminderToCoach() {
        log.debug("Request to send upcoming session reminder");
        //List<Session> sessions = sessionSchedulesRepository.findAllBySessionDate(LocalDate.now());
        List<Session> sessions = sessionRepository.findAllBysessionSchedules(LocalDate.now());

        for (Session session : sessions) {
            String smsContent;

            smsContent = "Hello " + session.getCoach().getFirstName()+",\n You have an upcoming session " + session.getName()+" with " +
                    " client: " + session.getClient().getFullName() + "\n The session will be " + session.getSessionVenue()+ " at "
                    + session.getSessionSchedules().getStartTime() + " to " + session.getSessionSchedules().getEndTime() + "\n See you there!";
            String smsContentClient;
            smsContentClient = "Hello " + session.getClient().getFirstName()+",\n You have an upcoming session" + session.getName()+"with " +
                    " coach: " + session.getCoach().getFullName() + "\n The session will be " + session.getSessionVenue()+ " at "
                    + session.getSessionSchedules().getStartTime() + "to " + session.getSessionSchedules().getEndTime() + "\n See you there!";
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

    public List<Session> getSessionByOrgId(Long orgId) {
        return sessionRepository.findSessionByOrgId(orgId);
    }

    public List<Session> getSessionsByContract(Long contractId) {
        return sessionRepository.findSessionByContractId(contractId);
    }

    public ListResponse filterSessionsByClientNameAndSessionNameAndDate(String clientName, String sessionName, LocalDate date, int page, int perPage) {
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);
        Page<SessionDto> sessionPage = null;

        if (clientName != null && sessionName != null && date != null) {
            QSession qSession = QSession.session;
            sessionPage = sessionRepository.findBy(qSession.client.fullName.containsIgnoreCase(clientName)
                    .and(qSession.name.containsIgnoreCase(sessionName))
                    .and(qSession.sessionSchedules.sessionDate.eq(date)), q -> q.sortBy(sort).as(SessionDto.class).page(pageable));
            log.info("This is a list of sessions found {}", sessionPage.getContent());
            return new ListResponse(sessionPage.getContent(), sessionPage.getTotalPages(), sessionPage.getNumberOfElements(),
                    sessionPage.getTotalElements());
        }

        if (clientName != null && sessionName != null) {
            QSession qSession = QSession.session;
            sessionPage = sessionRepository.findBy(qSession.client.fullName.containsIgnoreCase(clientName)
                    .and(qSession.name.containsIgnoreCase(sessionName)), q -> q.sortBy(sort).as(SessionDto.class).page(pageable));
            log.info("This is a list of sessions found {}", sessionPage.getContent());
            return new ListResponse(sessionPage.getContent(), sessionPage.getTotalPages(), sessionPage.getNumberOfElements(),
                    sessionPage.getTotalElements());
        }

        if (clientName != null && date != null) {
            QSession qSession = QSession.session;
            sessionPage = sessionRepository.findBy(qSession.client.fullName.containsIgnoreCase(clientName)
                    .and(qSession.sessionSchedules.sessionDate.eq(date)), q -> q.sortBy(sort).as(SessionDto.class).page(pageable));
            log.info("This is a list of sessions found {}", sessionPage.getContent());
            return new ListResponse(sessionPage.getContent(), sessionPage.getTotalPages(), sessionPage.getNumberOfElements(),
                    sessionPage.getTotalElements());
        }

        if (sessionName != null && date != null) {
            QSession qSession = QSession.session;
            sessionPage = sessionRepository.findBy(qSession.name.containsIgnoreCase(sessionName)
                    .and(qSession.sessionSchedules.sessionDate.eq(date)), q -> q.sortBy(sort).as(SessionDto.class).page(pageable));
            log.info("This is a list of sessions found {}", sessionPage.getContent());
            return new ListResponse(sessionPage.getContent(), sessionPage.getTotalPages(), sessionPage.getNumberOfElements(),
                    sessionPage.getTotalElements());
        }

        if (clientName != null) {
            QSession qSession = QSession.session;
            sessionPage = sessionRepository.findBy(qSession.client.fullName.containsIgnoreCase(clientName), q -> q.sortBy(sort).as(SessionDto.class).page(pageable));
            log.info("This is a list of sessions found {}", sessionPage.getContent());
            return new ListResponse(sessionPage.getContent(), sessionPage.getTotalPages(), sessionPage.getNumberOfElements(),
                    sessionPage.getTotalElements());
        }

        if (sessionName != null) {
            QSession qSession = QSession.session;
            sessionPage = sessionRepository.findBy(qSession.name.containsIgnoreCase(sessionName), q -> q.sortBy(sort).as(SessionDto.class).page(pageable));
            log.info("This is a list of sessions found {}", sessionPage.getContent());
            return new ListResponse(sessionPage.getContent(), sessionPage.getTotalPages(), sessionPage.getNumberOfElements(),
                    sessionPage.getTotalElements());
        }

        log.info("This is a list of sessions found {}", sessionPage.getContent());
        return new ListResponse(sessionPage.getContent(), sessionPage.getTotalPages(), sessionPage.getNumberOfElements(), sessionPage.getTotalElements());
    }

    @Transactional
    public void updateSessionStatus(Long id, Long coachId, SessionStatus sessionStatus) {
        log.info("Changing status of session {} to status {}", id, sessionStatus);
        Optional<Session> session = sessionRepository.findByIdAndCoach_id(id, coachId);

        if (session.isEmpty()) {
            throw new IllegalStateException("Coach doesn't exist");
        }

        Session session1 = session.get();

        if (session1.getSessionStatus() == SessionStatus.CANCELLED) {
            log.info("Session {} is in Cancelled state", id);
            throw new IllegalStateException("Session is in Cancelled State");
        } else if (Objects.equals(sessionStatus, SessionStatus.CONFIRMED)) {
            session1.setSessionStatus(SessionStatus.CONFIRMED);
        } else if (Objects.equals(sessionStatus, SessionStatus.CONDUCTED)) {
            session1.setSessionStatus(SessionStatus.CONDUCTED);
        } else if (Objects.equals(sessionStatus, SessionStatus.CANCELLED)) {
            session1.setSessionStatus(SessionStatus.CANCELLED);
        } else if (session1.getSessionStatus() == SessionStatus.CONFIRMED) {
            session1.setSessionStatus(SessionStatus.CONFIRMED);
        } else if (session1.getSessionStatus() == SessionStatus.CONDUCTED) {
            session1.setSessionStatus(SessionStatus.CONDUCTED);
        }

        log.info("Session with id {} changed status to {}", id, sessionStatus);
    }

    //CREATE EXAMPLE
    private Session createExample(String search, String status, Long coachId) {
        Session sessionExample = new Session();
        Session session = new Session();
        Client client = new Client();

        if(search != null && !search.isEmpty()) {
            sessionExample.setClient(client);
            sessionExample.setName(search);
            if (search.contains("@")) {
                sessionExample.getClient().getEmail();
            } else if (search.startsWith("+") || search.matches("[0-9]+")) {
                sessionExample.getClient().getMsisdn();
            } else {
                sessionExample.getName();
            }
        }
        if(status != null && !status.isEmpty()) {
            sessionExample.setSessionStatus(SessionStatus.valueOf(status));
        }

        return sessionExample;
    }

    public Page<SessionDto> filter(String search, String status, Long coachId, Pageable pageable) {
    Session session = createExample(search, status, coachId);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();
        Example<Session> example = Example.of(session, matcher);

        return sessionRepository.findAll(example, pageable);
    }
}
