package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.CoachingLog;
import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.CoachingLogRepository;
import com.natujenge.thecouch.repository.UserRepository;
import com.natujenge.thecouch.service.mapper.CoachingLogMapper;
import com.natujenge.thecouch.web.rest.dto.CoachingLogDTO;
import com.natujenge.thecouch.web.rest.request.CoachingLogRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional
public class CoachingLogService {

    private final UserRepository userRepository;
    private final CoachingLogRepository coachingLogRepository;
    private final CoachingLogMapper coachingLogMapper;

    public CoachingLogService(UserRepository userRepository, CoachingLogRepository coachingLogRepository, CoachingLogMapper coachingLogMapper) {
        this.userRepository = userRepository;
        this.coachingLogRepository = coachingLogRepository;
        this.coachingLogMapper = coachingLogMapper;
    }

    //CREATE
    public CoachingLog createCoachingLog(CoachingLogRequest coachingLogRequest, Long coachId
    ) {
        //find coach by id
        User coach = userRepository.findById(coachId).orElse(null);
        if (coach == null) {
            throw new IllegalArgumentException("Coach with id " + coachId + " not found");
        }

        log.info("Request to create coaching log , service layer");
        CoachingLog coachingLog = new CoachingLog();
        coachingLog.setClientName(coachingLogRequest.getClientName());
        coachingLog.setClientEmail(coachingLogRequest.getClientEmail());
        coachingLog.setNoInGroup(coachingLogRequest.getNoInGroup());
        coachingLog.setStartDate(coachingLogRequest.getStartDate());
        coachingLog.setEndDate(coachingLogRequest.getEndDate());

        coachingLog.setPaidHours(coachingLogRequest.getPaidHours());
        coachingLog.setProBonoHours(coachingLogRequest.getProBonoHours());

        coachingLog.setCreatedBy(coach.getFullName());
        coachingLog.setCreatedAt(LocalDateTime.now());

        coachingLog.setCoach(coach);

        return coachingLogRepository.save(coachingLog);

    }

    //READ
    private CoachingLog createExample(String search, Long coachId) {
        CoachingLog coachingLog = new CoachingLog();
        User coach = new User();
        if (search != null && !search.isEmpty()) {
            coachingLog.setClientName(search);
        }
        coachingLog.setCoach(coach);
        coachingLog.getCoach().setId(coachId);
        return coachingLog;
    }
    public Page<CoachingLogDTO> getAllCoachingLogs(String search, Pageable pageable, Long coachId) {
        CoachingLog coachingLog = createExample(search, coachId);

        log.info("Request to get all coaching logs");
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("coach.locked", "coach.enabled","coach.onboarded")
                .withIgnoreNullValues();

        Example<CoachingLog> example = Example.of(coachingLog, matcher);
        return coachingLogRepository.findAll(example, pageable).map(coachingLogMapper::toDto);

    }

    public void deleteCoachingLogs(List<Long> coachingLogIds, Long coachId) {
        log.info("Request to delete coaching logs");
        coachingLogRepository.deleteAllByIdInAndCoachId(coachingLogIds, coachId);
    }

    public void updateCoachLog(Session session, Long proBonoHours) {
        /*
        Creates a coaching log from a session once the session is marked as COMPLETED
        params: session
           extract the following from the session
              client details, coach details, session date, session time, paid hours
           and sets the details to the coaching log
        returns: void
        * */
        CoachingLog coachingLog = new CoachingLog();
        coachingLog.setClientName(session.getName());
        coachingLog.setClientEmail(session.getClient().getEmail());
        coachingLog.setNoInGroup(session.getSessionNumber());
        coachingLog.setLastUpdatedBy(session.getCoach().getFullName());
        if (session.getSessionSchedules().getSessionDate() != null) {
            coachingLog.setStartDate(Date.valueOf(session.getSessionSchedules().getSessionDate()));
            coachingLog.setEndDate(Date.valueOf(LocalDateTime.now().toLocalDate()));
        } else {
            coachingLog.setStartDate(Date.valueOf(session.getSessionDate()));
            coachingLog.setEndDate(Date.valueOf(session.getSessionDate()));
            // TODO: set paid hours over recurring sessions
        }
        coachingLog.setPaidHours(getTimeDelta(session.getSessionSchedules().getEndTime(), session.getSessionSchedules().getStartTime()));
        coachingLog.setProBonoHours(Objects.requireNonNullElse(proBonoHours, 0L));

        coachingLog.setCreatedBy(session.getCoach().getFullName());
        coachingLog.setCreatedAt(LocalDateTime.now());
        coachingLog.setCoach(session.getCoach());

        log.info("Coaching log to be saved: {}", coachingLog);
        coachingLogRepository.save(coachingLog);
    }

    private Long getTimeDelta(LocalTime endTime, LocalTime startTime) {
        /*
        * calculates the time difference between two LocalTime objects and returns the difference in hours
        * params: endTime, startTime
        * returns: Long (hours)
        * */
        return ChronoUnit.MINUTES.between(startTime, endTime) / 60;
    }
}
