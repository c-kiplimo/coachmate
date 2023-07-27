package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.DaysOfTheWeek;
import com.natujenge.thecouch.domain.SessionSchedules;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.SessionSchedulesRepository;
import com.natujenge.thecouch.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class SessionSchedulesService {
    private final SessionSchedulesRepository sessionSchedulesRepository;

    private final UserRepository userRepository;
    private final DaysOfTheWeekService daysOfTheWeekService;


    public SessionSchedulesService(SessionSchedulesRepository sessionSchedulesRepository, UserRepository userRepository, DaysOfTheWeekService daysOfTheWeekService) {
        this.sessionSchedulesRepository = sessionSchedulesRepository;
        this.userRepository = userRepository;
        this.daysOfTheWeekService = daysOfTheWeekService;
    }

    //Create session schedule
    public SessionSchedules createSessionSchedule(Long coachId, SessionSchedules sessionSchedules) throws IllegalArgumentException {
        log.info("Creating a new session schedule");
        Optional<User> optionalCoach = userRepository.findById(coachId);

        if (optionalCoach.isEmpty()) {
            log.warn("Coach with id {} not found", coachId);
            throw new IllegalArgumentException("Coach not found");
        }

        //get day of the week by id
        DaysOfTheWeek daysOfTheWeek = daysOfTheWeekService.getDaysOfTheWeekById(sessionSchedules.getDayOfTheWeek().getId());
        if (daysOfTheWeek == null) {
            log.warn("Day of the week with id {} not found", sessionSchedules.getDayOfTheWeek().getId());
            throw new IllegalArgumentException("Day of the week not found");
        }

        sessionSchedules.setSessionDate(sessionSchedules.getSessionDate());
        sessionSchedules.setCoach(optionalCoach.get());
        sessionSchedules.setStartTime(sessionSchedules.getStartTime());
        sessionSchedules.setEndTime(sessionSchedules.getEndTime());
        sessionSchedules.setOrgId(sessionSchedules.getOrgId());

        sessionSchedules.setDayOfTheWeek(daysOfTheWeek);

        sessionSchedules.setRecurring(sessionSchedules.getSessionDate() == null);

        sessionSchedules.setBooked(false);

        sessionSchedules.setCreatedAt(LocalDate.now());
        sessionSchedules.setUpdatedAt(LocalDate.now());


        try {
            return sessionSchedulesRepository.save(sessionSchedules);
        } catch (Exception e) {
            log.error("Error occurred ", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Optional<SessionSchedules> findSessionSchedulesById(Long sessionSchedulesId) {
        return sessionSchedulesRepository.findById(sessionSchedulesId);
    }

    public ResponseEntity<?> delete(Long id) {
        boolean exist = sessionSchedulesRepository.existsById(id);
        if (!exist) {
            throw new IllegalStateException("Schedule Not Found");
        }
        return sessionSchedulesRepository.findById(id)
                .map(sessionSchedules -> {
                    sessionSchedulesRepository.delete(sessionSchedules);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new IllegalArgumentException("Schedule Not Found"));
    }

    //UPDATE SCHEDULE TO BOOKED
    public SessionSchedules updateBookedState(Long id) {
        Optional<SessionSchedules> sessionSchedulesOptional = Optional.ofNullable(sessionSchedulesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule Not Found")));

        SessionSchedules sessionSchedules = sessionSchedulesOptional.get();
        if (sessionSchedules.isBooked()) {
            //UPDATE SCHEDULE TO UNBOOKED
            sessionSchedules.setBooked(false);
        } else {
            //UPDATE SCHEDULE TO BOOKED
            sessionSchedules.setBooked(true);
        }

        return sessionSchedulesRepository.save(sessionSchedules);
    }

    private SessionSchedules createExample(Long coachId) {
        SessionSchedules sessionExample = new SessionSchedules();
        User coach = new User();

        if (coachId != null) {
            sessionExample.setCoach(coach);
            sessionExample.getCoach().setId(coachId);
        }
        return sessionExample;
    }

    //findAllByCoachId
    public List<SessionSchedules> findAllByCoach(Long coachId) {

        //days of the week
        List<DaysOfTheWeek> daysOfTheWeek = daysOfTheWeekService.getDaysOfTheWeek(coachId);
        //get coach by id
        Optional<User> optionalCoach = userRepository.findById(coachId);
        if (optionalCoach.isEmpty()) {
            log.warn("Coach with id {} not found", coachId);
            throw new IllegalArgumentException("Coach not found");
        }
        log.info("Days of the week {}", daysOfTheWeek);
        //for each day of the week get the sessions schedules
        //store the sessions schedules in an array
        List<SessionSchedules> allSessionSchedules = new ArrayList<>();
        for (DaysOfTheWeek day : daysOfTheWeek) {
            log.info("Day of the week {}", day);
            //get all sessions schedules for the day of the week and coach
            //find by coach and day of the week
            List<SessionSchedules> sessionSchedules = sessionSchedulesRepository.findByDayOfTheWeekAndCoach(day, optionalCoach.get());
            log.info("Sessions schedules {}", sessionSchedules);
            //add the sessions schedules to the array
            allSessionSchedules.addAll(sessionSchedules);
        }
        //return array of objects with sessions schedules
        return allSessionSchedules;

    }


    public List<DaysOfTheWeek> getDaysOfTheWeek(Long coachId) {
        return daysOfTheWeekService.getDaysOfTheWeek(coachId);
    }

    public DaysOfTheWeek updateDayOfTheWeek(Long id, DaysOfTheWeek daysOfTheWeek) {
        return daysOfTheWeekService.updateDayOfTheWeek(id, daysOfTheWeek);
    }


    public SessionSchedules updateSessionSchedule(Long id, SessionSchedules sessionSchedules) {
        Optional<SessionSchedules> sessionSchedulesOptional = Optional.ofNullable(sessionSchedulesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule Not Found")));

        SessionSchedules sessionSchedules1 = sessionSchedulesOptional.get();
        sessionSchedules1.setSessionDate(sessionSchedules.getSessionDate());
        sessionSchedules1.setStartTime(sessionSchedules.getStartTime());
        sessionSchedules1.setEndTime(sessionSchedules.getEndTime());
        sessionSchedules1.setOrgId(sessionSchedules.getOrgId());
        sessionSchedules1.setBooked(sessionSchedules.isBooked());
        sessionSchedules1.setRecurring(sessionSchedules.isRecurring());
        sessionSchedules1.setUpdatedAt(LocalDate.now());

        return sessionSchedulesRepository.save(sessionSchedules1);
    }
}
