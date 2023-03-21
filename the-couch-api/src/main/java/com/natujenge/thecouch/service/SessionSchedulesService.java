package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.domain.SessionSchedules;
import com.natujenge.thecouch.repository.CoachRepository;
import com.natujenge.thecouch.repository.SessionSchedulesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
@Transactional

public class SessionSchedulesService {
    @Autowired
    SessionSchedulesRepository sessionSchedulesRepository;

    @Autowired
    CoachRepository coachRepository;

    //Get all schedule by coach id
    public SessionSchedules findSessionSchedulesByCoachId(Long id) {
        log.debug("Request to get SessionSchedules : {} by coach id {}", id);

        Optional<SessionSchedules> sessionSchedules = sessionSchedulesRepository.findAllByCoachId(id);

        if (sessionSchedules.isPresent()){
            return sessionSchedules.get();
        } else {
            throw new IllegalStateException("Session schedules not founs!");

        }

    }

    //Create session schedule
    public SessionSchedules createSessionSchedule(Long coachId, SessionSchedules sessionSchedules) throws IllegalArgumentException{
        log.info("Creating a new session schedule");
        Optional<Coach> optionalCoach = coachRepository.findCoachById(coachId);

        if(optionalCoach.isEmpty()){
            log.warn("Coach with id {} not found", coachId );
            throw new IllegalArgumentException("Coach not found");
        }

        sessionSchedules.setSessionDate(sessionSchedules.getSessionDate());
        sessionSchedules.setCoach(optionalCoach.get());
        sessionSchedules.setEndTime(sessionSchedules.getEndTime());
        sessionSchedules.setStartTime(sessionSchedules.getStartTime());
        sessionSchedules.setOrgId(sessionSchedules.getOrgId());

        try {
            return sessionSchedulesRepository.save(sessionSchedules);
        }catch (Exception e) {
            log.error("Error occurred ", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
