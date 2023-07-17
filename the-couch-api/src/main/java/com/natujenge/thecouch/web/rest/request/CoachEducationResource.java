package com.natujenge.thecouch.web.rest.request;


import com.natujenge.thecouch.domain.CoachEducation;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.CoachEducationRepository;
import com.natujenge.thecouch.service.CoachEducationService;
import com.natujenge.thecouch.util.PaginationUtil;
import com.natujenge.thecouch.web.rest.dto.CoachEducationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(path = "/api/coachEducation")

public class CoachEducationResource {

    private final CoachEducationService coachEducationService;

    private final CoachEducationRepository coachEducationRepository;

    public CoachEducationResource(CoachEducationService coachEducationService, CoachEducationRepository coachEducationRepository) {
        this.coachEducationService = coachEducationService;
        this.coachEducationRepository = coachEducationRepository;
    }

    //GET: All  /api/coachEducation
    @GetMapping("/allCoachEducation")
    public ResponseEntity<List<CoachEducationDTO>> getAllCoachEducation(@AuthenticationPrincipal User userDetails,
                                                     @RequestParam(value = "search", required = false) String search,
                                                     Pageable pageable) {
        Long coachId = userDetails.getId();
        if (coachId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            log.info("Request to get all coach education, {}", coachId);
            Page<CoachEducationDTO> coachEducationList = coachEducationService.getAllCoachEducation(coachId, search, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), coachEducationList);
            return ResponseEntity.ok().headers(headers).body(coachEducationList.getContent());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
            log.info("Create CoachEducation request received: {}", coachEducation);

            CoachEducation coachEducationResponse = coachEducationService.createCoachEducation(coachEducation);
            return new ResponseEntity<>(coachEducationResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //PUT  /api/coachEducation
    @PutMapping
    public ResponseEntity<?> updateCoachEducation(@RequestBody CoachEducation coachEducation) {

        try{
            log.info("Change CoachEducation request received: {}", coachEducation);
            Optional<CoachEducation> coachEducationResponse = coachEducationService.updateCoachEducation(coachEducation);
            return new ResponseEntity<>(coachEducationResponse, HttpStatus.OK);
        } catch (Exception e) {
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
