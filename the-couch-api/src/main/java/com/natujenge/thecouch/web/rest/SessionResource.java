package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Contract;
import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.SessionStatus;
import com.natujenge.thecouch.service.ContractService;
import com.natujenge.thecouch.service.SessionService;
import com.natujenge.thecouch.util.PaginationUtil;
import com.natujenge.thecouch.util.ResponseUtil;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.dto.SessionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(path = "/api/sessions")
public class SessionResource {
    private final SessionService sessionService;
    private final ContractService contractService;

    public SessionResource(SessionService sessionService, ContractService contractService) {
        this.sessionService = sessionService;
        this.contractService = contractService;
    }


    //POST: /api/sessions
    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody Session session,
                                           @RequestParam("contractId") Long contractId,
                                           @RequestParam("clientId") Long clientId,
                                           @AuthenticationPrincipal User userDetails) {
        log.info("Request to create session");
        try {
            // check if the number of sessions has been exceeded
            Contract contract = contractService.findContractById(contractId);
            Long sessionsCount = sessionService.countSessionByContractId(contractId);
            log.info("sessions {}", sessionsCount);
            if (sessionsCount >= contract.getNoOfSessions()) {
                return new ResponseEntity<>(new RestResponse(false, "Cannot create session: maximum number of sessions has been reached"), HttpStatus.BAD_REQUEST);
            }

            Session sessionResponse = sessionService.createSession(userDetails.getId(), clientId, contractId, session);
            return new ResponseEntity<>(new RestResponse(false, "Session Created Successfully"), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //GET: Get individual sessions by id
    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> getSessionById(@PathVariable("id") Long id,
                                                     @AuthenticationPrincipal User userDetails) {

        Optional<SessionDTO> session = sessionService.findSessionById(id);
        return ResponseUtil.wrapOrNotFound(session);

    }

    // UPDATE SESSION DETAILS
    @PutMapping(path = "/{id}")
    public ResponseEntity<SessionDTO> updateSession(@RequestBody Session session,
                                                    @PathVariable("id") Long id,
                                                    @AuthenticationPrincipal User userDetails) {

        log.info("Update Session request received");
        SessionDTO updatedSession = sessionService.updateSession(id, session);
        return new ResponseEntity<>(updatedSession, HttpStatus.OK);

    }

    //DELETE:/api/sessions/:id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable("id") Long id,
                                          @AuthenticationPrincipal User userDetails) {
        sessionService.deleteSession(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/change-status/{id}")
        // change status active or suspend
    ResponseEntity<SessionDTO> updateSessionStatus(@RequestParam("status") SessionStatus sessionStatus,
                                                   @PathVariable Long id,
                                                   @AuthenticationPrincipal User userDetails) {

        log.info("request to change session status with id : {} to status {} by coach with id {}", id, sessionStatus, userDetails.getId());
        SessionDTO sessionDTO = sessionService.updateSessionStatus(id, sessionStatus);
        return new ResponseEntity<>(sessionDTO, HttpStatus.OK);

    }

    @GetMapping("/filter")
    public ResponseEntity<List<SessionDTO>> filterSessions(@RequestParam(name = "coachId", required = false) Long coachId,
                                                           @RequestParam(name = "clientId", required = false) Long clientId,
                                                           @RequestParam(name = "contractId", required = false) Long contractId,
                                                           @RequestParam(name = "sessionStatus", required = false) String sessionStatus,
                                                           @RequestParam(name = "sessionDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate sessionDate,
                                                           @RequestParam(name = "search", required = false) String search,
                                                           @RequestParam(name="orgId", required = false) Long orgId,
                                                           Pageable pageable,
                                                           @AuthenticationPrincipal User userDetails) {
        Long organisationId = null;
        if (orgId != null) {
            organisationId = orgId;
        } else
            if (userDetails.getOrganization() != null) {
                organisationId = userDetails.getOrganization().getId();
            }

        Page<SessionDTO> sessionDtoPage = sessionService.filter(coachId, clientId, contractId, sessionStatus, sessionDate, organisationId, search, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), sessionDtoPage);
        return ResponseEntity.ok().headers(headers).body(sessionDtoPage.getContent());
    }

}