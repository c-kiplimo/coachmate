package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.domain.SessionSchedules;
import com.natujenge.thecouch.repository.CoachRepository;
import com.natujenge.thecouch.repository.SessionSchedulesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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
    public List<SessionSchedules> findSessionSchedulesByCoachId(Long id, Boolean status) {
        log.debug("Request to get SessionSchedules : {} by coach id {}", id, status);

        Optional<Coach> optionalCoach = coachRepository.findCoachById(id);
        if(status == null) {
            log.info("Request to get session schedule");
            return sessionSchedulesRepository.findByCoach(optionalCoach.get());
        } else {
            log.info("Request to get session schedule");
            return sessionSchedulesRepository.findByCoachAndBooked(optionalCoach.get(), status);
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

    public SessionSchedules delete(Long id, Long coachId) {
    boolean exist = sessionSchedulesRepository.existsByIdAndCoachId(id, coachId);
    if(!exist) {
        throw new IllegalStateException("Schedule Not Found");
    }
    sessionSchedulesRepository.deleteById(id);
    return null;
    }

    //UPDATE SCHEDULE TO BOOKED
    public Optional<SessionSchedules> updateBookedStateToTrue(Long id) {
        Optional<SessionSchedules> sessionSchedules = Optional.ofNullable(sessionSchedulesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule Not Found")));

        SessionSchedules sessionSchedules1 = sessionSchedules.get();
        sessionSchedules1.setBooked(true);

        sessionSchedules1 = sessionSchedulesRepository.save(sessionSchedules1);
        return Optional.of(sessionSchedules1);
    }
    //UPDATE SCHEDULE TO UNBOOKED
    public Optional<SessionSchedules> updateBookedStateToFalse(Long id) {
        Optional<SessionSchedules> sessionSchedules = Optional.ofNullable(sessionSchedulesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule Not Found")));

        SessionSchedules sessionSchedules1 = sessionSchedules.get();
        sessionSchedules1.setBooked(false);

        sessionSchedules1 = sessionSchedulesRepository.save(sessionSchedules1);
        return Optional.of(sessionSchedules1);
    }
}
