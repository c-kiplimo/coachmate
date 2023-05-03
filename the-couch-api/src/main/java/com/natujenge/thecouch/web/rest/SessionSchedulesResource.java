package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.SessionSchedules;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.service.SessionSchedulesService;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/api/sessionSchedules")
public class SessionSchedulesResource {
    private final SessionSchedulesService sessionSchedulesService;
    public SessionSchedulesResource(SessionSchedulesService sessionSchedulesService) {
        this.sessionSchedulesService = sessionSchedulesService;
    }

    //Get by coach id
    @GetMapping("/byCoachId/{coachId}")
    public ResponseEntity<?> findSessionSchedulesByCoachId (@PathVariable("coachId") Long coachId,
                                                            @RequestParam(name = "status", required = false) Boolean status,
                                                            @AuthenticationPrincipal User userDetails) {
        try {
            List<SessionSchedules> sessionSchedules = sessionSchedulesService.findSessionSchedulesByCoachId(coachId, status);
            return new ResponseEntity<>(sessionSchedules, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //ADD
    @PostMapping
    public ResponseEntity<?> createSessionSchedule (@RequestBody SessionSchedules sessionSchedules,
                                                    @RequestParam("coachId") Long coachId,
                                                    @AuthenticationPrincipal User userDetails) {
        log.info("Request to create session schedule");
        try{
            SessionSchedules sessionSchedules1 = sessionSchedulesService.createSessionSchedule(coachId, sessionSchedules);
            return new ResponseEntity<>(new RestResponse(false, "Session schedule Created"), HttpStatus.CREATED);
        } catch(Exception e) {
            log.error("Error", e);
            return new ResponseEntity<>( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //DELETE
    @DeleteMapping
    ResponseEntity delete(@RequestParam(name = "id") Long id, @RequestParam(name = "coachId") Long coachId) {
        SessionSchedules deleteSessionSchedule = sessionSchedulesService.delete(id, coachId);
        return new ResponseEntity(deleteSessionSchedule, HttpStatus.OK);
    }


}
