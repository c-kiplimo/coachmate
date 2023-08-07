package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.CoachingLog;
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

import java.time.LocalDateTime;
import java.util.List;

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
}
