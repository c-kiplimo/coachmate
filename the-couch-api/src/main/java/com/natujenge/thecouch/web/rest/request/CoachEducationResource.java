package com.natujenge.thecouch.web.rest.request;


import com.natujenge.thecouch.domain.CoachEducation;
import com.natujenge.thecouch.repository.CoachEducationRepository;
import com.natujenge.thecouch.service.CoachEducationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(path = "/api/coachEducation")

public class CoachEducationResource {

    @Autowired
    CoachEducationService coachEducationService;

    @Autowired
    CoachEducationRepository coachEducationRepository;

    //GET: All  /api/coachEducation
    @PostMapping("/getCoachEducation")
    public ResponseEntity<?> getAllCoachEducation(@RequestBody CoachEducation coachEducation) {
        try{
            List<CoachEducation> coachEducationList = coachEducationService.getAllCoachEducation(coachEducation);

            return new ResponseEntity<>(coachEducationList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("..........................................", e);
            return new ResponseEntity<>("Error Occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //GET by id  /api/coachEducation/:id

    @GetMapping("/{id}")
    public ResponseEntity<?> getCoachEducationById (@PathVariable("id") Long id){

        Optional<CoachEducation> coachEducation = coachEducationRepository.findCoachEducationById(id);
        return new ResponseEntity<>(coachEducation, HttpStatus.OK);
    }

    //ADD --- /api/coachEducation
    @PostMapping
    public ResponseEntity<?> createCoachEducation(@RequestBody CoachEducation coachEducation){
        try{
            log.info("Coach Creation request Received: ", coachEducation);

            CoachEducation coachEducationResponse = coachEducationService.createCoachEducation(coachEducation);
            return new ResponseEntity<>(coachEducationResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(">........................", e);
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //PUT  /api/coachEducation
    @PutMapping
    public ResponseEntity<?> updateCoachEducation(@RequestBody CoachEducation coachEducation) {

        try{
            log.info("Change CoachEducation request Received :", coachEducation);
            Optional<CoachEducation> coachEducationResponse = coachEducationService.updateCoachEducation(coachEducation);
            return new ResponseEntity<>(coachEducationResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("..................................................", e);
            return new ResponseEntity<>("Error occurred white updating coach education", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


    //DELETE : /api/coachEducation
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoachEducation(@PathVariable("id") Long id) {

        coachEducationService.deleteCoachEducation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
