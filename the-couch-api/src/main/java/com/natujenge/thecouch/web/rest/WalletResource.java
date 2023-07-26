package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.ClientWallet;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.StatementPeriod;
import com.natujenge.thecouch.domain.enums.UserRole;
import com.natujenge.thecouch.service.ClientWalletService;
import com.natujenge.thecouch.service.WalletService;
import com.natujenge.thecouch.util.PaginationUtil;
import com.natujenge.thecouch.web.rest.dto.ClientWalletDTO;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.dto.SessionDTO;
import com.natujenge.thecouch.web.rest.request.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "api/wallet")
public class WalletResource {

    @Autowired
    ClientWalletService clientWalletService;


    @Autowired
    WalletService walletService;



    // Create payments based on logged-in user
    @PostMapping
    ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest,
                                     @AuthenticationPrincipal User userDetails) {
        log.info("Request to create payment");
        log.info("userDetails{}",userDetails.getId());

        Long coachId = null;
        Long clientId = null;
        Long organizationId = null;

        try {
            log.info("Logged in user {} ", userDetails);
            String userRole = userDetails.getUserRole().name();
            switch (userRole) {
                case "COACH":
                    coachId = userDetails.getId();
                    break;
                case "CLIENT":
                    clientId = userDetails.getId();
                    break;
                case "ORGANIZATION":
                    organizationId = userDetails.getId();
                    break;
                default:
                    return new ResponseEntity<>(new RestResponse(true, "You are not authorized to perform this action"),
                            HttpStatus.UNAUTHORIZED);
            }

//            Long coachId = (userDetails == null) ? null : userDetails.getId();
//            log.info("coach id {}",coachId);
//            Long organizationId = (userDetails.getOrganization() == null) ? null : userDetails.getOrganization().getId();
//            log.info("org id {}",organizationId);
//            Long clientId = (userDetails == null) ? null : userDetails.getId();
//            log.info("client id {}",coachId);

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

//     Get all payments based on logged-in user
//    @GetMapping
//    ResponseEntity<?> getAllPayments(@RequestParam("per_page") int perPage,
//                                     @RequestParam("page") int page,
//                                     @AuthenticationPrincipal User userDetails) {
//        log.info("Request all payments");
//        try {
//            Long coachId = (userDetails == null) ? null : userDetails.getId();
//            Long organizationId = (userDetails.getOrganization() == null) ? null :
//                    userDetails.getOrganization().getId();
//            Long clientId = (userDetails == null) ? null : userDetails.getId();
//
//
//
//            ListResponse listResponse;
//            if (organizationId != null) {
//                listResponse = walletService.getPaymentsByOrganizationId
//                        (page, perPage, organizationId);
//            } else if (coachId != null) {
//                listResponse = walletService.getPaymentsByCoachId
//                        (page, perPage, coachId);
//            } else {
//                listResponse = walletService.getPaymentsByClientId
//                        (page, perPage, clientId);
//            }
//
//            return new ResponseEntity<>(listResponse, HttpStatus.OK);
//
//        } catch (Exception e) {
//            log.error("Error ", e);
//            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//    @GetMapping("/filterByCoachId")
//    public ResponseEntity<?> filterByCoachId(
//            @RequestParam("per_page") int perPage,
//            @RequestParam("page") int page,
//            @AuthenticationPrincipal User userDetails,
//            @RequestParam(name = "coachId") Long coachId
//    ) {
//        try {
//            log.info("Request to get payments by coach Id");
//            ListResponse listResponse = walletService.getPaymentsByCoachId(page, perPage, coachId);
//
//            return new ResponseEntity<>(listResponse, HttpStatus.OK);
//
//        } catch (Exception e) {
//            log.error("Error occurred ", e);
//            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
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

    @GetMapping("/filter")
    public ResponseEntity<List<ClientWalletDTO>> filterPayments(@RequestParam(name = "coachId", required = false) Long coachId,
                                                           @RequestParam(name = "clientId", required = false) Long clientId,
                                                           @RequestParam(name = "statementPeriod", required = false) String statementPeriod,
                                                           @RequestParam(name = "search", required = false) String search,
                                                           @RequestParam(name="orgId", required = false) Long organizationId,
                                                           Pageable pageable,
                                                           @AuthenticationPrincipal User userDetails) {
        log.info("Request to filter payments");
        Page<ClientWalletDTO> clientWalletDTOPage = walletService.filter(coachId, clientId, statementPeriod, organizationId, search, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), clientWalletDTOPage);
        return ResponseEntity.ok().headers(headers).body(clientWalletDTOPage.getContent());
    }


}
