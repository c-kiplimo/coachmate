package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.CoachEducation;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.CoachEducationRepository;
import com.natujenge.thecouch.service.mapper.CoachEducationMapper;
import com.natujenge.thecouch.web.rest.dto.CoachEducationDTO;
import com.querydsl.core.types.Predicate;
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

    public CoachEducationService(CoachEducationRepository coachEducationRepository, CoachEducationMapper coachEducationMapper) {
        this.coachEducationRepository = coachEducationRepository;
        this.coachEducationMapper = coachEducationMapper;
    }

    //CREATE
    public CoachEducation createCoachEducation(CoachEducation coachEducation) {

        try{
            User coach = new User();
            coachEducation.setCoach(coach);
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
            exampleCoachEducation.setProvider(search);
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
