package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.domain.Contract;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.SessionStatus;
import com.natujenge.thecouch.service.ContractService;
import com.natujenge.thecouch.service.SessionService;
import com.natujenge.thecouch.web.rest.dto.ListResponse;

import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.dto.SessionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasLength;

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


    //GET: /sessions
    @GetMapping
    public ResponseEntity<?> getSessions(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @RequestParam(name = "search", required = false) String search,
            @AuthenticationPrincipal User userDetails
    ){
        try {
            ListResponse listResponse = sessionService.getAllSessions(page, perPage, search, userDetails.getId());
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/filter")
//    public ResponseEntity<SessionDto> ResponseEntity<List<SessionDto>> filterSessions(@RequestParam(name = "search") String search,
//                                                                                      @RequestParam(name = "status") String status,
//                                                                                      Pageable pageable,
//                                                                                      @AuthenticationPrincipal User userDetails) {3
//        Long coachId = userDetails.getCoach().getId();
//        Page<SessionDto> sessionDtoPage = sessionService.filter(search, status, coachId, pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), sessionDtoPage);
//        return ResponseEntity.ok().headers(headers).body(sessionDtoPage.getContent());
//    }


    //GET: Get individual sessions by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getSessionById (@PathVariable("id") Long id,
                                             @AuthenticationPrincipal User userDetails) {

        try {
            Session sessionRequest = sessionService.findSessionByIdAndCoachId(id);
            return new ResponseEntity<>(sessionRequest, HttpStatus.OK);

        } catch (Exception e) {
            log.error("error ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //POST: /api/sessions
    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody Session session,
                                           @RequestParam("contractId") Long contractId,
                                           @RequestParam("clientId") Long clientId,
                                           @AuthenticationPrincipal User userDetails){
        log.info("Request to create session");
        try{
            // check if the number of sessions has been exceeded
            Contract contract = contractService.getContract(contractId);
            List<Session> sessions = sessionService.getSessionsByContract(contractId);
            log.info("sessions {}" , sessions);
            int sessionCount = sessions.size();
            if (sessionCount >= contract.getNoOfSessions()) {
                return new ResponseEntity<>(new RestResponse(false, "Cannot create session: maximum number of sessions has been reached"), HttpStatus.BAD_REQUEST);
            }

            Session sessionResponse = sessionService.createSession(userDetails.getId(),clientId,contractId,session);
            return new ResponseEntity<>(new RestResponse(false,"Session Created Successfully"), HttpStatus.CREATED);
        } catch(Exception e) {
            log.error("Error", e);
            return new ResponseEntity<>( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // UPDATE SESSION DETAILS
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateSession(@RequestBody Session session,
                                           @PathVariable("id") Long id,
                                           @AuthenticationPrincipal User userDetails){
        try{
            log.info("Update Session request received");
            Optional<Session> updatedSession = sessionService.updateSession(id,userDetails.getId(),session);
            if (updatedSession.isEmpty()){
                throw new NoSuchElementException();
            }
            return new ResponseEntity<>(updatedSession.get(), HttpStatus.OK);

        } catch(Exception e) {
            log.error(".....", e);
            return new ResponseEntity<>( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //DELETE:/api/sessions/:id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable("id") Long id,
                                          @AuthenticationPrincipal User userDetails) {
        sessionService.deleteSession(id,userDetails.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //GET: Get client sessions by client_id
    @GetMapping("/clientSessions/{clientId}")
        public ResponseEntity<?> getClientSessions (@PathVariable("clientId") Long clientId,
                                                    @AuthenticationPrincipal User userDetails) {
        try {
            List<SessionDto> session = sessionService.findSessionByClientId(clientId);
            return new ResponseEntity<>(session, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        }

        //GET ORGANIZATION SESSIONS
    @GetMapping(path = "/getorgSessions/{orgId}")
    ResponseEntity<?> getOrgSessions(@PathVariable("orgId") Long orgId,
                                     @AuthenticationPrincipal User userDetails){
        log.info("Request to get Organization sessions", orgId);

        try {
            List<Session> listResponse = sessionService.getSessionByOrgId(orgId);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new RestResponse(true, "Error Occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //GET: Get contracts sessions by contract_id
    @GetMapping("/contractSessions/{contractId}")
    public ResponseEntity<?> getContractsSessions (@PathVariable("contractId") Long contractId,
                                                @AuthenticationPrincipal User userDetails) {
        try {
            List<SessionDto> session = sessionService.findSessionByContractId(contractId);
            return new ResponseEntity<>(session, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/filterSessions")
    public ResponseEntity<ListResponse> filterSessionsByClientNameAndSessionNameAndDate(
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) String sessionName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage) {

        log.info("Request sessions");
        try {
            ListResponse listResponse = sessionService.filterSessionsByClientNameAndSessionNameAndDate(clientName, sessionName, date, page, perPage);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        }
        catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity(new RestResponse(true, "Error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PutMapping(path = "/change-status/{id}") // change status active or suspend
    ResponseEntity<?> updateSessionStatus(@RequestParam("status") SessionStatus sessionStatus,
                                          @PathVariable Long id,
                                          @AuthenticationPrincipal User userDetails) {
        try{
            log.info("request to change session status with id : {} to status {} by coach with id {}", id, sessionStatus,userDetails.getId());
            sessionService.updateSessionStatus(id,userDetails.getId(), sessionStatus);

            return new ResponseEntity<>(new RestResponse(false, "Session status updated successfully"), HttpStatus.OK);
        } catch (Exception e){
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "Session status not updated, contact admin"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}