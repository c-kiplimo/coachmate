package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.CoachEducation;
import com.natujenge.thecouch.repository.CoachEducationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@Transactional
public class CoachEducationService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CoachEducationRepository coachEducationRepository;

    //CREATE
    public CoachEducation createCoachEducation(CoachEducation coachEducation) {

        try{
            coachEducationRepository.save(coachEducation);

            return coachEducation;
        } catch (Exception e) {
            log.error("Error Occurred while creating a new coach education", e);

            return null;
        }

    }

    //GET ALL
    public List<CoachEducation> getAllCoachEducation(CoachEducation coachEducation) {
        return coachEducationRepository.findAllByCoachId(coachEducation.getCoachId());
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
