package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Payment;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.service.PaymentService;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.dto.PaymentDto;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.request.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
@RestController
@Slf4j
@RequestMapping("api/payments")
public class PaymentResource {
    private final PaymentService paymentService;

    public PaymentResource(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    // Get all Payments
    @GetMapping
    ResponseEntity<?> getAllPayments(@RequestParam("per_page") int perPage,
                                     @RequestParam("page") int page,
                                     @AuthenticationPrincipal User userDetails) {
        log.info("Request all payments");
        try {
            ListResponse listResponse = paymentService.getAllPayments(page, perPage, userDetails.getCoach().getId());
            return new ResponseEntity<>(listResponse, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity<>(new RestResponse(true, "Error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Get payment By Id
    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable(name = "id") Long id,
                                            @AuthenticationPrincipal User userDetails) {
        log.info("Requesting payment by id {}", id);
        try {
            Optional<PaymentDto> paymentOptional = paymentService.getPaymentById(id,userDetails.getCoach().getId());

            if (paymentOptional.isPresent()) {
                return ResponseEntity.ok().body(paymentOptional.get());
            } else {
                return new ResponseEntity<>(new RestResponse(true, "Payment not found"),
                        HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true,
                    "Payment could not be fetched, contact admin")
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    // Create payment
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest,
                                           @AuthenticationPrincipal User userDetails) {
        log.info("Request to create payment");
        try {
            Payment newPayment = paymentService.createPayment(paymentRequest,userDetails.getCoach()
                    .getId());
            if (newPayment != null) {
                URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/payments")
                        .toUriString());
                return ResponseEntity.created(uri).body(newPayment);
            } else {
                return new ResponseEntity<>(new RestResponse(true, "Payment added, but link failure"),
                        HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity<>(new RestResponse(true, "Payment not added, contact admin"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // update payment
    @PutMapping(path = "/{id}")
    ResponseEntity<?> updatePayment(@RequestBody PaymentRequest paymentRequest,
                                    @PathVariable Long id,
                                    @AuthenticationPrincipal User userDetails) {
        log.info("Request to update Payment with id : {} by coach of id {}", id,userDetails.getCoach()
                .getId());
        try {
            Optional<Payment> updatedPayment;
            updatedPayment = paymentService.updatePayment(id, userDetails.getCoach().getId(),
                    userDetails.getCoach().getFullName(),paymentRequest);

            return new ResponseEntity<>(new RestResponse(false, "Payment updated successfully"),
                    HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "Payment not updated, contact admin"),
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
            Long coachId = userDetails.getCoach().getId();
            log.debug(
                    "REST request to filter payments given, coach id : {}, client id  : {}",
                    clientId,
                    coachId

            );

            ListResponse payments = paymentService.filterByClientIdAndCoachId(
                    page,
                    perPage,
                    clientId,
                    coachId
            );
            //LATER add HTTP headers
            return ResponseEntity.ok().body(payments);
        } catch (Exception e){
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "An Error occurred, contact admin"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    // Filter payments by ClientId
    @GetMapping("/filter-by-session-id")
    public ResponseEntity<?> filterPaymentsBySessionIdAndCoachId(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "session_id",required = false) Long sessionId
    ) {
        try {
            Long coachId = userDetails.getCoach().getId();
            log.debug(
                    "REST request to filter payments given, coach id : {}, session id  : {}",
                    coachId,
                    sessionId
            );
            ListResponse payments = paymentService.filterBySessionIdAndCoachId(
                    page,
                    perPage,
                    sessionId,
                    coachId
            );
            //LATER add HTTP headers
            return ResponseEntity.ok().body(payments);
        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "An Error occurred, contact admin"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
