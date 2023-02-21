package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.ClientWallet;
import com.natujenge.thecouch.domain.Payment;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.service.WalletService;
import com.natujenge.thecouch.web.rest.dto.ClientWalletDto;
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
    WalletService walletService;

    // create payment based on coach/organization
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest,
                                           @AuthenticationPrincipal User userDetails) {
        log.info("Request to create payment");
        try {
            ClientWallet wallet = walletService.createPayment(paymentRequest, userDetails.getCoach());

            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/payments")
                    .toUriString());
            return ResponseEntity.created(uri).body(wallet);

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
            Long organizationId = (userDetails.getCoach().getId() == null) ? userDetails.getOrganization().getId() :
                    null;
            Long coachId = (userDetails.getOrganization().getId() == null) ? userDetails.getCoach().getId() :
                    null;
            Long clientId = (userDetails.getClient().getId() == null) ? userDetails.getCoach().getId() :
                    null;

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
}
