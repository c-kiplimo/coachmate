package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.CoachEducation;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.CoachEducationRepository;
import com.natujenge.thecouch.repository.UserRepository;
import com.natujenge.thecouch.service.mapper.CoachEducationMapper;
import com.natujenge.thecouch.web.rest.dto.CoachEducationDTO;
import com.natujenge.thecouch.web.rest.request.CoachEducationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@Service
@Slf4j
@Transactional
public class CoachEducationService {

    private final CoachEducationRepository coachEducationRepository;
    private final CoachEducationMapper coachEducationMapper;
    private final UserRepository userRepository;

    public CoachEducationService(CoachEducationRepository coachEducationRepository, CoachEducationMapper coachEducationMapper, UserRepository userRepository) {
        this.coachEducationRepository = coachEducationRepository;
        this.coachEducationMapper = coachEducationMapper;
        this.userRepository = userRepository;
    }

    //CREATE
    public CoachEducation createCoachEducation(CoachEducationRequest coachEducationRequest, Long coachId) {

        try{
            log.info("Request to create a new coach education");

            Optional<User> coach = userRepository.findById(coachId);
            if (coach.isEmpty()) {
                log.error("Coach with id {} does not exist", coachId);
                return null;
            }
            CoachEducation coachEducation = new CoachEducation();
            coachEducation.setCoach(coach.get());
            coachEducation.setCourseName(coachEducationRequest.getCourseName());
            coachEducation.setProvider(coachEducationRequest.getProvider());
            coachEducation.setValidTill(coachEducationRequest.getValidTill());
            coachEducation.setCertificateUrl(coachEducationRequest.getCertificateUrl());
            coachEducation.setTrainingHours(coachEducationRequest.getTrainingHours());
            coachEducation.setDateIssued(coachEducationRequest.getDateIssued());
            coachEducation.setCreatedBy(coachEducationRequest.getCreatedBy());

            coachEducationRepository.save(coachEducation);
            return coachEducation;
        } catch (Exception e) {
            log.error("Error Occurred while creating a new coach education", e);

            return null;
        }

    }

    //GET ALL
    private CoachEducation createExampleCoachEducation(Long coachId, String search) {
        CoachEducation exampleCoachEducation = new CoachEducation();
        User coach = new User();
        if (coachId != null) {
            exampleCoachEducation.setCoach(coach);
            exampleCoachEducation.getCoach().setId(coachId);
        }
         if (search != null && !search.isEmpty()) {
            exampleCoachEducation.setCourseName(search);
        }
        return exampleCoachEducation;
    }
    public Page<CoachEducationDTO> getAllCoachEducation(Long coachId, String search, Pageable pageable) {
        log.info("Request to get all coach education");
        CoachEducation exampleCoachEducation = createExampleCoachEducation(coachId, search);
        log.info("Example CoachEducation: {}", exampleCoachEducation);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("coach.locked", "coach.enabled","coach.onboarded")
                .withIgnoreNullValues();
        Example<CoachEducation> example = Example.of(exampleCoachEducation, matcher);
        return coachEducationRepository.findAll(example, pageable).map(coachEducationMapper::toDto);
    }

    //Get ONE CoachEducation
    public Optional<CoachEducation> findCoachEducationById(long id) {
        return coachEducationRepository.findCoachEducationById(id);
    }

    //DELETE
    public void deleteCoachEducation(Long id) {
        coachEducationRepository.deleteCoachEducationById(id);

    }

    //UPDATE
public Optional<CoachEducation> updateCoachEducation(CoachEducation coachEducation) {

        Optional<CoachEducation> coachEducationOptional = coachEducationRepository.findCoachEducationById(coachEducation.getId());

        log.info("CoachEducation found with id: ", coachEducation.getId());

        if (coachEducationOptional.isPresent()) {
            CoachEducation coachEducation1 = coachEducationOptional.get();

            coachEducation1.setCourseName(coachEducation.getCourseName());
            coachEducation1.setCertificateUrl(coachEducation.getCertificateUrl());
            coachEducation1.setProvider(coachEducation.getProvider());
            coachEducation1.setDateIssued(coachEducation.getDateIssued());
            coachEducation1.setTrainingHours(coachEducation.getTrainingHours());
            coachEducation1.setValidTill(coachEducation.getValidTill());
            coachEducation1.setLastUpdatedBy(coachEducation.getLastUpdatedBy());

            coachEducation1 = coachEducationRepository.save(coachEducation1);
            log.info("Updated CoachEducation with id: ", coachEducation1.getId());
            return Optional.of(coachEducation1);
        }
        return coachEducationOptional;
}
}
