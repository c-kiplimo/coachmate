package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.ClientWallet;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.StatementPeriod;
import com.natujenge.thecouch.service.ClientWalletService;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.request.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Slf4j
@RequestMapping(path = "api/wallet")
public class WalletResource {

    @Autowired
    ClientWalletService walletService;



    // Create payments based on logged-in user
    @PostMapping
    ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest,
                                     @AuthenticationPrincipal User userDetails) {
        log.info("Request to create payment");
        log.info("userDetails{}",userDetails.getId());
        try {
            log.info("Logged in user {} ",userDetails);
            Long coachId = (userDetails == null) ? null : userDetails.getId();
            log.info("coach id {}",coachId);
            Long organizationId = (userDetails.getOrganization() == null) ? null : userDetails.getOrganization().getId();
            log.info("org id {}",organizationId);
            Long clientId = (userDetails == null) ? null : userDetails.getId();
            log.info("client id {}",coachId);

            if (organizationId != null) {
                log.info("Request to create payment by organization");
                try {
                    log.info("request to create payment");
                    ClientWallet wallet = walletService.createPaymentByOrganization(paymentRequest,
                            userDetails.getOrganization());

                    URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/payments")
                            .toUriString());
                    return ResponseEntity.created(uri).body(wallet);

                } catch (Exception e) {
                    log.error("Error ", e);
                    return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                            HttpStatus.INTERNAL_SERVER_ERROR);
                }
//            } else if (clientId != null) {
//                log.info("Request to create payment by client");
//                try {
//                    ClientWallet wallet = walletService.createPaymentByClient(paymentRequest, userDetails);
//
//                    URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/payments")
//                            .toUriString());
//                    return ResponseEntity.created(uri).body(wallet);
//
//                } catch (Exception e) {
//                    log.error("Error ", e);
//                    return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
//                            HttpStatus.INTERNAL_SERVER_ERROR);
//                }

            } else if (coachId != null) {
                log.info("Request to create payment by coach");
                try {
                    ClientWallet wallet = walletService.createPaymentByCoach(paymentRequest, userDetails);

                    URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/payments")
                            .toUriString());
                    return ResponseEntity.created(uri).body(wallet);

                } catch (Exception e) {
                    log.error("Error ", e);
                    return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                            HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>(new RestResponse(true, "You are not authorized to perform this action"),
                        HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all payments based on logged-in user
    @GetMapping
    ResponseEntity<?> getAllPayments(@RequestParam("per_page") int perPage,
                                     @RequestParam("page") int page,
                                     @AuthenticationPrincipal User userDetails) {
        log.info("Request all payments");
        try {
            Long coachId = (userDetails == null) ? null : userDetails.getId();
            Long organizationId = (userDetails.getOrganization() == null) ? null :
                    userDetails.getOrganization().getId();
            Long clientId = (userDetails == null) ? null : userDetails.getId();



            ListResponse listResponse;
            if (organizationId != null) {
                listResponse = walletService.getPaymentsByOrganizationId
                        (page, perPage, organizationId);
            } else if (coachId != null) {
                listResponse = walletService.getPaymentsByCoachId
                        (page, perPage, coachId);
            } else {
                listResponse = walletService.getPaymentsByClientId
                        (page, perPage, clientId);
            }

            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/filterByCoachId")
    public ResponseEntity<?> filterByCoachId(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "coachId") Long coachId
    ) {
        try {
            log.info("Request to get payments by coach Id");
            ListResponse listResponse = walletService.getPaymentsByCoachId(page, perPage, coachId);

            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/filterByOrgId")
    public ResponseEntity<?> filterByOrgId(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "organizationId") Long organizationId
    ) {
        try {
            log.info("Request to get payments by organization Id");
            ListResponse listResponse = walletService.getPaymentsByOrganizationId(page, perPage, organizationId);

            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Get payments by client id and coach id order by desc
    @GetMapping("/filter-by-client-id")
    public ResponseEntity<?> filterPaymentsByClientIdAndCoachId(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "client_id",required = false) Long clientId
    ) {
        try {

            log.debug(
                    "REST request to filter payments given, coach id : {}, client id  : {}",
                    clientId


            );

            ListResponse listResponse;
                listResponse = walletService.getPaymentsByClientId
                        (page, perPage, clientId);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e){
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @GetMapping("/filterReceipts")
    public ResponseEntity<ListResponse> filterByClientName(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage) {

        log.info("Request receipts");
        try {
            ListResponse listResponse = walletService.filterByClientName(page, perPage,name);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        }
        catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity(new RestResponse(true, "Error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //  get payments by coach id and statement period
    @GetMapping("/filterByCoachIdAndClientAndStatementPeriod")
    public ResponseEntity<?> filterByCoachIdAndStatementPeriod(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @RequestParam(name = "search",required = false) String search,
            @RequestParam(name = "clientName",required = false) String clientName,
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "statement_period",required = false) StatementPeriod statementPeriod

    ) {
        try {
            Long coachId = userDetails.getId();
            log.info("Request to get account statement by coach Id {} and statement period {}", coachId, statementPeriod);
            ListResponse listResponse = walletService.getPaymentsByCoachIdAndStatementPeriod(page,perPage, coachId,search,clientName, statementPeriod);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //  get payments by client id and statement period
    @GetMapping("/filterByClientIdAndStatementPeriod")
    public ResponseEntity<?> filterByClientIdAndStatementPeriod(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "statement_period",required = false) StatementPeriod statementPeriod

    ) {
        try {
            Long clientId = userDetails.getId();
            log.info("Request to get account statement by client Id {} and statement period {}", clientId, statementPeriod);
            ListResponse listResponse = walletService.getPaymentsByClientIdAndStatementPeriod(page,perPage, clientId, statementPeriod);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //  get payments by organization id and statement period
    @GetMapping("/filterByOrganizationIdAndClientAndStatementPeriod")
    public ResponseEntity<?> filterByOrganizationIdAndStatementPeriod(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @RequestParam(name="search", required=false) String search,
            @RequestParam(name = "clientName",required = false) String clientName,
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "statement_period",required = false) StatementPeriod statementPeriod

    ) {
        try {
            Long organizationId = userDetails.getOrganization().getId();
            log.info("Request to get account statement by organization Id {} and statement period {}", organizationId, statementPeriod);
            ListResponse listResponse = walletService.getPaymentsByOrganizationIdAndClientAndStatementPeriod(page,perPage,organizationId, search,clientName, statementPeriod);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}
