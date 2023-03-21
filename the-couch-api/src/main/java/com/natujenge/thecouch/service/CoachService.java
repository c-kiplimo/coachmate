package com.natujenge.thecouch.service;
import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.enums.CoachStatus;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.CoachRepository;
import com.natujenge.thecouch.repository.OrganizationRepository;
import com.natujenge.thecouch.web.rest.request.CoachRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CoachService {

    private final CoachRepository coachRepository;
    private final OrganizationRepository organizationRepository;
  @Autowired
    private RegistrationService registrationService;

    //     constructor
    public CoachService(CoachRepository coachRepository, OrganizationRepository organizationRepository, RegistrationService registrationService) {
        this.coachRepository = coachRepository;
        this.organizationRepository = organizationRepository;
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

    // create coach by organization
    public Coach addNewCoachByOrganization(Long organizationId, CoachRequest coachRequest) {
        log.info("add a new coach to database");

        Optional<Organization> optionalOrganization = organizationRepository.getOrganizationById(organizationId);


        Coach coach = new Coach();

        if (optionalOrganization.isPresent()) {
            coach.setOrganization(optionalOrganization.get());
            coach.setFirstName(coachRequest.getFirstName());
            coach.setLastName(coachRequest.getLastName());
            coach.setFullName(coachRequest.getFirstName() + " " + coachRequest.getLastName());
            coach.setMsisdn(coachRequest.getMsisdn());
            coach.setEmailAddress(coachRequest.getEmail());
            coach.setPassword(coach.getPassword());

        }

        Coach saveCoach = coachRepository.save(coach);
        //save user
        registrationService.registerCoachAsUser(saveCoach);
        return saveCoach;


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
    public Coach findCoachById(long id) {
        return coachRepository.findCoachById(id)
                .orElseThrow(() -> new UserNotFoundException("Coach by id " + id + " not found"));
    }

    //DELETE a coach
    public void deleteCoach(Long id){
        coachRepository.deleteCoachById(id);
    }

    public List<Coach> getCoachByOrganizationId(Long organizationId) {
        return coachRepository.findByOrganizationId(organizationId);
    }
}
