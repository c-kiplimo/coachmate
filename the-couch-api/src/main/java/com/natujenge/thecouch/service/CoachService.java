package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.domain.enums.CoachStatus;
import com.natujenge.thecouch.repository.CoachRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

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
}
