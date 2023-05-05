package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.SessionSchedules;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.service.SessionSchedulesService;
import com.natujenge.thecouch.util.PaginationUtil;
import com.natujenge.thecouch.util.ResponseUtil;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.dto.SessionSchedulesDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    @GetMapping("/{id}")
    public ResponseEntity<SessionSchedulesDTO> getSessionSchedulesById(@PathVariable("id") Long id,
                                                     @AuthenticationPrincipal User userDetails) {

        Optional<SessionSchedulesDTO> sessionSchedules = sessionSchedulesService.findSessionSchedulesById(id);
        return ResponseUtil.wrapOrNotFound(sessionSchedules);

    }
    //DELETE
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam(name = "id") Long id) {
        sessionSchedulesService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //Get by coach id
    @GetMapping("/filter")
    public ResponseEntity<?> filter (@RequestParam(name = "coachId", required = false) Long coachId,
                                     @RequestParam(name = "status", required = false) Boolean status,
                                     @RequestParam(name = "search", required = false) String search,
                                     Pageable pageable,
                                     @AuthenticationPrincipal User userDetails) {
        Page<SessionSchedulesDTO> sessionSchedulesDTOPage = sessionSchedulesService.filter(coachId, status, search, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), sessionSchedulesDTOPage);
        return ResponseEntity.ok().headers(headers).body(sessionSchedulesDTOPage.getContent());
    }


}
