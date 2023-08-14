package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.ContractStatus;
import com.natujenge.thecouch.domain.enums.SessionStatus;
import com.natujenge.thecouch.domain.enums.SessionType;
import com.natujenge.thecouch.domain.enums.SessionVenue;
import com.natujenge.thecouch.repository.*;
import com.natujenge.thecouch.service.mapper.SessionMapper;
import com.natujenge.thecouch.service.notification.NotificationServiceHTTPClient;
import com.natujenge.thecouch.util.NotificationHelper;
import com.natujenge.thecouch.web.rest.dto.SessionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.criteria.JoinType;


@Service
@Slf4j
@Transactional
public class SessionService {

    private final JdbcTemplate jdbcTemplate;
    private final SessionRepository sessionRepository;
    private final ContractRepository contractRepository;
    private final OrganizationRepository organizationRepository;
    private final SessionSchedulesRepository sessionSchedulesRepository;
    private final NotificationServiceHTTPClient notificationServiceHTTPClient;
    private final SessionSchedulesService sessionSchedulesService;
    private final UserRepository userRepository;
    private final SessionMapper sessionMapper;

    public SessionService(JdbcTemplate jdbcTemplate, SessionRepository sessionRepository, ContractRepository contractRepository, OrganizationRepository organizationRepository, SessionSchedulesRepository sessionSchedulesRepository, NotificationServiceHTTPClient notificationServiceHTTPClient, SessionSchedulesService sessionSchedulesService, UserRepository userRepository, SessionMapper sessionMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.sessionRepository = sessionRepository;
        this.contractRepository = contractRepository;
        this.organizationRepository = organizationRepository;
        this.sessionSchedulesRepository = sessionSchedulesRepository;
        this.notificationServiceHTTPClient = notificationServiceHTTPClient;
        this.sessionSchedulesService = sessionSchedulesService;
        this.userRepository = userRepository;
        this.sessionMapper = sessionMapper;
    }

    //CREATE NEW SESSION
    public Session createSession(Long coachId, Long clientId, Long contractId, Session sessionRequest) throws IllegalArgumentException {
        log.info("Creating new session");

        Optional<User> optionalClient = userRepository.findById(clientId);
        Optional<Contract> optionalContract = contractRepository.findById(contractId);
        Optional<SessionSchedules> optionalSessionSchedules = sessionSchedulesRepository.findById(sessionRequest.getSessionSchedules().getId());

        if (optionalClient.isEmpty()) {
            log.warn("Client with id {} not found", clientId);
            throw new IllegalArgumentException("Client not found!");
        }

        if (optionalContract.isEmpty()) {
            log.warn("Contract with id {} not found", contractId);
            throw new IllegalArgumentException("Contract not found!");
        }
        if (optionalSessionSchedules.isEmpty()) {
            log.warn("Session slot with Id {} not found", sessionRequest.getSessionSchedules().getId());
            throw new IllegalArgumentException("Session Slot no found!");
        }
        if (optionalContract.get().getContractStatus() == ContractStatus.NEW) {
            log.warn("Contract with id {} is in NEW state", contractId);
            throw new IllegalArgumentException("Contract is in not signed!");
        }
        // Client
        User client = optionalClient.get();
        // Coach
        User coach = client.getAddedBy();
        // Contract
        Contract contract = optionalContract.get();
        //Session slot
        SessionSchedules sessionSchedules = optionalSessionSchedules.get();

        // New Sessions enter the new state
        sessionRequest.setSessionStatus(SessionStatus.NEW);
        sessionRequest.setName(sessionRequest.getName());
        sessionRequest.setSessionType(SessionType.valueOf(String.valueOf(sessionRequest.getSessionType())));
        sessionRequest.setSessionVenue(SessionVenue.valueOf(String.valueOf(sessionRequest.getSessionVenue())));
        sessionRequest.setCoach(coach);
        sessionRequest.setClient(client);
        sessionRequest.setContract(contract);
        sessionRequest.setSessionSchedules(sessionSchedules);
        sessionRequest.setSessionDate(sessionSchedules.getSessionDate() != null ? sessionSchedules.getSessionDate() : sessionRequest.getSessionDate());
        sessionRequest.setCreatedBy(coach.getFullName());
        sessionRequest.setLastUpdatedBy(coach.getFullName());
        // session Number Generation
        int randNo = (int) ((Math.random() * (999 - 1)) + 1);
        String sessionL = String.format("%05d", randNo);
        String sessionNo = client.getAddedBy().getBusinessName().substring(0, 2) +
                client.getFirstName().charAt(0) + client.getLastName().charAt(0) + "-" + sessionL;
        sessionRequest.setSessionNumber(sessionNo);
        sessionRequest.setOrganization(sessionRequest.getOrganization());

        try {
            sessionSchedulesService.updateBookedState(sessionSchedules.getId());
            return sessionRepository.save(sessionRequest);
        } catch (Exception e) {
            log.error("Error occurred ", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    // Get Individual Sessions by id
    public Optional<SessionDTO> findSessionById(Long sessionId){
       return sessionRepository.findById(sessionId).map(sessionMapper::toDto);
    }

    //UPDATE SESSION
    public SessionDTO updateSession(Long sessionId, Session sessionRequest) {

        Optional<Session> sessionOptional = sessionRepository.findById(sessionId);

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
            session = sessionRepository.save(session);
            return sessionMapper.toDto(session);

        }
        return sessionMapper.toDto(sessionOptional.get());
    }

    // Delete session by ID
    public void deleteSession(Long id) {
        log.debug("Request to delete session : {}", id);


        boolean exist = sessionRepository.existsById(id);
        if (!exist) {
            throw new IllegalStateException("session doesn't exist");

        }
        sessionRepository.deleteById(id);
    }

    //send notification to client and coach when session is upcoming
    @Scheduled(cron = "0 0 6 * * *")
    public void sendUpcomingSessionReminderToCoach() {
        log.debug("Request to send upcoming session reminder");
        //List<Session> sessions = sessionSchedulesRepository.findAllBySessionDate(LocalDate.now());
        List<Session> sessions = sessionRepository.findAllBySessionSchedulesSessionDate(LocalDate.now());

        for (Session session : sessions) {
            String smsContent;

            smsContent = "Hello " + session.getCoach().getFirstName() + ",\n You have an upcoming session " + session.getName() + " with " +
                    " client: " + session.getClient().getFullName() + "\n The session will be " + session.getSessionVenue() + " at "
                    + session.getSessionSchedules().getStartTime() + " to " + session.getSessionSchedules().getEndTime() + "\n See you there!";
            String smsContentClient;
            smsContentClient = "Hello " + session.getClient().getFirstName() + ",\n You have an upcoming session" + session.getName() + "with " +
                    " coach: " + session.getCoach().getFullName() + "\n The session will be " + session.getSessionVenue() + " at "
                    + session.getSessionSchedules().getStartTime() + "to " + session.getSessionSchedules().getEndTime() + "\n See you there!";
            //send notification to coach
            NotificationHelper.sendUpcomingSessionReminderToCoach(session);
            // sendEmail
            String email = session.getCoach().getEmail();
            notificationServiceHTTPClient.sendEmail(email, "SESSION DUE TODAY", smsContent, false);
            log.info("Email sent");
            //send notification to client
            NotificationHelper.sendUpcomingSessionReminderToClient(session);
            // sendEmail
            String clientEmail = session.getClient().getEmail();
            notificationServiceHTTPClient.sendEmail(clientEmail, "SESSION DUE TODAY", smsContentClient, false);
        }

    }
    @Transactional
    public SessionDTO updateSessionStatus(Long id, SessionStatus sessionStatus) {
        log.info("Changing status of session {} to status {}", id, sessionStatus);
        Optional<Session> sessionOptional = sessionRepository.findById(id);

        if (sessionOptional.isEmpty()) {
            throw new IllegalStateException("Session doesn't exist");
        }

        Session session = sessionOptional.get();

        if (session.getSessionStatus() == SessionStatus.CANCELLED) {
            log.info("Session {} is in Cancelled state", id);
            throw new IllegalStateException("Session is in Cancelled State");
        } else if (Objects.equals(sessionStatus, SessionStatus.CONFIRMED)) {
            session.setSessionStatus(SessionStatus.CONFIRMED);
        } else if (Objects.equals(sessionStatus, SessionStatus.COMPLETED)) {
            session.setSessionStatus(SessionStatus.COMPLETED);
        } else if (Objects.equals(sessionStatus, SessionStatus.CANCELLED)) {
            session.setSessionStatus(SessionStatus.CANCELLED);
        } else if (session.getSessionStatus() == SessionStatus.CONFIRMED) {
            session.setSessionStatus(SessionStatus.CONFIRMED);
        } else if (session.getSessionStatus() == SessionStatus.COMPLETED) {
            session.setSessionStatus(SessionStatus.COMPLETED);
        }
        session = sessionRepository.save(session);
        log.info("Session with id {} changed status to {}", id, sessionStatus);
        return sessionMapper.toDto(session);

    }

    public long countSessionByContractId(Long contractId){
        return sessionRepository.countByContractId(contractId);
    }
    private Session createExample(Long coachId, Long clientId, Long contractId, String sessionStatus,Long organisationId, String search) {
        Session sessionExample = new Session();
        User client = new User();
        User coach = new User();
        Contract contract = new Contract();

        if (coachId != null) {
            sessionExample.setCoach(coach);
            sessionExample.getCoach().setId(coachId);
        }
        if (clientId != null) {
            sessionExample.setClient(client);
            sessionExample.getClient().setId(clientId);
        }
        if (contractId != null) {
            sessionExample.setContract(contract);
            sessionExample.getContract().setId(contractId);
        }
        if (sessionStatus != null && !sessionStatus.isEmpty()) {
            SessionStatus status = SessionStatus.valueOf(sessionStatus);
            sessionExample.setSessionStatus(status);
        }
        if (organisationId != null) {
            sessionExample.setOrganization(new Organization());
            sessionExample.getOrganization().setId(organisationId);
        }
        if (search != null && !search.isEmpty()) {
            sessionExample.setClient(client);
            if (search.contains("@")) {
                sessionExample.getClient().setEmail(search);
            } else if (search.startsWith("+") || search.matches("[0-9]+")) {
                sessionExample.getClient().setMsisdn(search);
            } else {
                sessionExample.getClient().setFullName(search);
            }
        }
        return sessionExample;
    }

    public Page<SessionDTO> filter(Long coachId, Long clientId, Long contractId,String sessionStatus, LocalDate sessionDate,  Long organisationId, String search, Pageable pageable) {
        Session session = createExample(coachId, clientId, contractId, sessionStatus, organisationId, search);
        log.info("After example {} ", session);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("coach.locked", "coach.enabled","coach.onboarded", "client.locked", "client.enabled","client.onboarded")
                .withIgnoreNullValues();
        Example<Session> example = Example.of(session, matcher);

        if (sessionDate != null) {
            log.info("Session date at {} ", sessionDate);
            return sessionRepository.findAll(getSpecsFromDatesAndExample(sessionDate, example), pageable).map(sessionMapper::toDto);
        }
        return sessionRepository.findAll(example, pageable).map(sessionMapper::toDto);
    }


    public Specification<Session> getSpecsFromDatesAndExample(LocalDate sessionDate, Example<Session> example) {
        /*
        Extract the predicates from the example and add the sessionDate predicate
        Parameters:
            sessionDate - the sessionDate to match
            example - the example to use for matching

        Returns:
            the specification to use for querying
        * */
        return (Specification<Session>) (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (sessionDate != null) {
                // Add the predicate to match sessionDate when not null
                predicates.add(builder.equal(
                        root.get("sessionDate"), sessionDate
                ));
            }

            if (example != null) {
                // Add the predicate to match example when not null
                predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, builder, example));
            }

            // Join sessionSchedules and add the predicate when sessionSchedules is not null
            Join<Object, Object> sessionSchedulesJoin = root.join("sessionSchedules", JoinType.LEFT);
            predicates.add(builder.or(
                    builder.equal(sessionSchedulesJoin.get("sessionDate"), sessionDate),
                    builder.isNull(sessionSchedulesJoin.get("sessionDate"))
            ));

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
