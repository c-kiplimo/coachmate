package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.service.AccountStatementService;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.domain.enums.StatementPeriod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(path = "api/statement")

public class AccountStatementResource {
    @Autowired
    AccountStatementService accountStatementService;

    @GetMapping("/filterByCoachId")
    public ResponseEntity<?> filterByCoachId(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails

    ) {
        try {
            Long coachId = userDetails.getCoach().getId();
            log.info("Request to get billing account by coach Id {}", coachId);
            ListResponse listResponse = accountStatementService.getStatementByCoachId(page,perPage, coachId);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // get billing account by organization id
    @GetMapping("/filterByOrganizationId")
    public ResponseEntity<?> filterByOrganizationId(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails

    ) {
        try {
            Long organizationId = userDetails.getOrganization().getId();
            log.info("Request to get account statement by organization Id {}", organizationId);
            ListResponse listResponse = accountStatementService.getStatementByOrganizationId( page,perPage, organizationId);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // get statement by client id
    @GetMapping("/filterByClientId")
    public ResponseEntity<?> filterByClientId(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "client_id",required = false) Long clientId

    ) {
        try {
            if(clientId == null){
                clientId = userDetails.getClient().getId();
            }
            log.info("Request to get account statement by client Id {}", clientId);
            ListResponse listResponse = accountStatementService.getStatementByClientId( page,perPage, clientId);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // get statement by coach id and client id
    @GetMapping("/filterByCoachIdAndClientId")
    public ResponseEntity<?> filterByCoachIdAndClientId(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails

    ) {
        try {
            Long coachId = userDetails.getCoach().getId();
            Long clientId = userDetails.getClient().getId();
            log.info("Request to get account statement by coach Id {} and client Id {}", coachId, clientId);
            ListResponse listResponse = accountStatementService.getStatementByCoachIdAndClientId( page,perPage, coachId, clientId);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // get statement by organization id and client id and statement period
    @GetMapping("/filterByOrganizationIdAndClientIdAndStatementPeriod")
    public ResponseEntity<?> filterByOrganizationIdAndClientIdAndStatementPeriod(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "statement_period",required = false) StatementPeriod statementPeriod

    ) {
        try {
            Long organizationId = userDetails.getOrganization().getId();
            Long clientId = userDetails.getClient().getId();
            log.info("Request to get account statement by organization Id {} and client Id {} and statement period {}", organizationId, clientId, statementPeriod);
            ListResponse listResponse = accountStatementService.getStatementByOrganizationIdAndClientIdAndStatementPeriod(page,perPage, organizationId, clientId, statementPeriod);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // get statement by organization id and client id
    @GetMapping("/filterByOrganizationIdAndClientId")
    public ResponseEntity<?> filterByOrganizationIdAndClientId(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails

    ) {
        try {
            Long organizationId = userDetails.getOrganization().getId();
            Long clientId = userDetails.getClient().getId();
            log.info("Request to get account statement by organization Id {} and client Id {}", organizationId, clientId);
            ListResponse listResponse = accountStatementService.getStatementByOrganizationIdAndClientId(page,perPage, organizationId, clientId);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // get statement by coach id and client id and statement period
    @GetMapping("/filterByCoachIdAndClientIdAndStatementPeriod")
    public ResponseEntity<?> filterByCoachIdAndClientIdAndStatementPeriod(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "statement_period",required = false) StatementPeriod statementPeriod

    ) {
        try {
            Long coachId = userDetails.getCoach().getId();
            Long clientId = userDetails.getClient().getId();
            log.info("Request to get account statement by coach Id {} and client Id {} and statement period {}", coachId, clientId, statementPeriod);
            ListResponse listResponse = accountStatementService.getStatementByCoachIdAndClientIdAndStatementPeriod(page,perPage, coachId, clientId, statementPeriod);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //  get statement by coach id and statement period
    @GetMapping("/filterByCoachIdAndStatementPeriod")
    public ResponseEntity<?> filterByCoachIdAndStatementPeriod(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "statement_period",required = false) StatementPeriod statementPeriod

    ) {
        try {
            Long coachId = userDetails.getCoach().getId();
            log.info("Request to get account statement by coach Id {} and statement period {}", coachId, statementPeriod);
            ListResponse listResponse = accountStatementService.getStatementByCoachIdAndStatementPeriod(page,perPage, coachId, statementPeriod);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}