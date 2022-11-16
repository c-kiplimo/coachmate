package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.domain.enums.CoachStatus;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.CoachRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CoachService {

    private final CoachRepository coachRepository;

//     constructor
    public CoachService(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }



    // add new coach
    public Coach addNewCoach(Coach coach) {
        if (coach.getId() == null) {
            coach.setStatus(CoachStatus.NEW);
            coach.setCreatedAt(LocalDateTime.now());
            coach.setCreatedBy(coach.getFullName());
            coach.setReason("DEFAULT");
        }
        return coachRepository.save(coach);
    }

    //UPDATE
    public Coach updateCoach(Coach coach) {
        try {
            log.info("Received an update request for {}", coach.getFullName());
            coachRepository.save(coach);
            return coach;
        } catch (Exception e) {
            log.error("Error occurred", e);
            return null;
        }
    }
    //INDEX - all coaches
    public List<Coach> viewCoaches() {
        return coachRepository.findAll();
    }

    //SHOW - one coach
    public Client findCoachById(long id) {
        return coachRepository.findCoachById(id)
                .orElseThrow(() -> new UserNotFoundException("Coach by id " + id + " not found"));
    }

    //DELETE a coach
    public void deleteCoach(Long id){
        coachRepository.deleteCoachById(id);
    }
}
