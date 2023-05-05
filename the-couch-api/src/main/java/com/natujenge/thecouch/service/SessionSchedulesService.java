package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.SessionSchedules;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.SessionStatus;
import com.natujenge.thecouch.repository.SessionSchedulesRepository;
import com.natujenge.thecouch.repository.UserRepository;
import com.natujenge.thecouch.service.dto.SessionDTO;
import com.natujenge.thecouch.service.dto.SessionSchedulesDTO;
import com.natujenge.thecouch.service.mapper.SessionSchedulesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional

public class SessionSchedulesService {
    private final SessionSchedulesRepository sessionSchedulesRepository;

    private final UserRepository userRepository;

    private final SessionSchedulesMapper sessionSchedulesMapper;
    public SessionSchedulesService(SessionSchedulesRepository sessionSchedulesRepository, UserRepository userRepository, SessionSchedulesMapper sessionSchedulesMapper) {
        this.sessionSchedulesRepository = sessionSchedulesRepository;
        this.userRepository = userRepository;
        this.sessionSchedulesMapper = sessionSchedulesMapper;
    }

    //Create session schedule
    public SessionSchedules createSessionSchedule(Long coachId, SessionSchedules sessionSchedules) throws IllegalArgumentException{
        log.info("Creating a new session schedule");
        Optional<User> optionalCoach = userRepository.findById(coachId);

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

    public Optional<SessionSchedulesDTO> findSessionSchedulesById(Long sessionSchedulesId){
        return sessionSchedulesRepository.findById(sessionSchedulesId).map(sessionSchedulesMapper::toDto);
    }
    public void delete(Long id) {
    boolean exist = sessionSchedulesRepository.existsById(id);
    if(!exist) {
        throw new IllegalStateException("Schedule Not Found");
    }
    sessionSchedulesRepository.deleteById(id);
    }

    //UPDATE SCHEDULE TO BOOKED
    public SessionSchedulesDTO updateBookedState(Long id) {
        Optional<SessionSchedules> sessionSchedulesOptional = Optional.ofNullable(sessionSchedulesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule Not Found")));

        SessionSchedules sessionSchedules = sessionSchedulesOptional.get();
        if(sessionSchedules.isBooked()){
            //UPDATE SCHEDULE TO UNBOOKED
            sessionSchedules.setBooked(false);
        }
        else {
            //UPDATE SCHEDULE TO BOOKED
            sessionSchedules.setBooked(true);
        }

        sessionSchedules = sessionSchedulesRepository.save(sessionSchedules);
        return sessionSchedulesMapper.toDto(sessionSchedules);
    }

    private SessionSchedules createExample(Long coachId,Boolean bookedStatus, String search) {
        SessionSchedules sessionExample = new SessionSchedules();
        User coach = new User();

        if (coachId != null) {
            sessionExample.setCoach(coach);
            sessionExample.getCoach().setId(coachId);
        }
        if (bookedStatus != null) {
            sessionExample.setBooked(bookedStatus);
        }

        if (search != null && !search.isEmpty()) {
            sessionExample.setCoach(coach);
            if (search.contains("@")) {
                sessionExample.getCoach().setEmail(search);
            } else if (search.startsWith("+") || search.matches("[0-9]+")) {
                sessionExample.getCoach().setMsisdn(search);
            } else {
                sessionExample.getCoach().setFullName(search);
            }
        }
        return sessionExample;
    }

    public Page<SessionSchedulesDTO> filter(Long coachId, Boolean bookedStatus, String search, Pageable pageable) {
        SessionSchedules sessionSchedules = createExample(coachId, bookedStatus, search);
        log.info("After example {} ", sessionSchedules);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths(".*\\.(locked|enabled)")
                .withIgnoreNullValues();
        Example<SessionSchedules> example = Example.of(sessionSchedules, matcher);

        return sessionSchedulesRepository.findAll(example, pageable).map(sessionSchedulesMapper::toDto);
    }

}
