package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.DaysOfTheWeek;
import com.natujenge.thecouch.domain.SessionSchedules;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.service.SessionSchedulesService;
import com.natujenge.thecouch.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(path = "/api/sessionSchedules")
public class SessionSchedulesResource {
    private final SessionSchedulesService sessionSchedulesService;
    public SessionSchedulesResource(SessionSchedulesService sessionSchedulesService) {
        this.sessionSchedulesService = sessionSchedulesService;
    }

    //ADD
    @PostMapping
    public SessionSchedules createSessionSchedule (@RequestBody SessionSchedules sessionSchedules,
                                                   @RequestParam("coachId") Long coachId,
                                                   @AuthenticationPrincipal User userDetails) {
        log.info("Request to create session schedule");
        try{
            return sessionSchedulesService.createSessionSchedule(coachId, sessionSchedules);
        } catch(Exception e) {
            log.error("Error", e);
            return null;
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<SessionSchedules> getSessionSchedulesById(@PathVariable("id") Long id,
                                                                    @AuthenticationPrincipal User userDetails) {

        Optional<SessionSchedules> sessionSchedules = sessionSchedulesService.findSessionSchedulesById(id);
        return ResponseUtil.wrapOrNotFound(sessionSchedules);

    }
    //DELETE
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam(name = "id") Long id) {
       return sessionSchedulesService.delete(id);

    }

    //Get by coach id
    @GetMapping("/filter")
    public ResponseEntity<?> filter (@RequestParam(name = "coachId", required = false) Long coachId,
                                                     @AuthenticationPrincipal User userDetails) {
        log.info("Request to filter session schedules");
        //find all session schedules by coach id
        List<SessionSchedules> sessionSchedules = sessionSchedulesService.findAllByCoach(coachId);
        return new ResponseEntity<>(sessionSchedules, HttpStatus.OK);

    }

    //update session schedule
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSessionSchedule (@RequestBody SessionSchedules sessionSchedules,
                                                    @PathVariable("id") Long id,
                                                    @AuthenticationPrincipal User userDetails) {
        log.info("Request to update session schedule");
        try {
            SessionSchedules sessionSchedules1 = sessionSchedulesService.updateSessionSchedule(id, sessionSchedules);
            return new ResponseEntity<>(sessionSchedules1, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //GET Day of the week
    @GetMapping("/daysOfTheWeek")
    public ResponseEntity<?> getDaysOfTheWeek (
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "coachId", required = false) Long coachId) {
        log.info("Request to get day of the week");
        //find all session schedules by day of the week
        List<DaysOfTheWeek> sessionSchedules = sessionSchedulesService.getDaysOfTheWeek(coachId);
        return new ResponseEntity<>(sessionSchedules, HttpStatus.OK);

    }
    //update day of the week
    @PutMapping("/daysOfTheWeek/{id}")
    public ResponseEntity<?> updateDayOfTheWeek (@RequestBody DaysOfTheWeek daysOfTheWeek,
                                                 @PathVariable("id") Long id,
                                                 @AuthenticationPrincipal User userDetails) {
        log.info("Request to update day of the week");
        try {
            DaysOfTheWeek daysOfTheWeek1 = sessionSchedulesService.updateDayOfTheWeek(id, daysOfTheWeek);
            return new ResponseEntity<>(daysOfTheWeek1, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
