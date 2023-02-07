package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Contract;
import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.service.ClientService;
import com.natujenge.thecouch.service.ContractService;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.request.ContractRequest;
import com.natujenge.thecouch.web.rest.request.SessionRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(path = "api/contracts")
public class ContractResource {
    @Autowired
    ContractService contractService;

    // Get All Contracts
    @GetMapping
    public ResponseEntity<?> getContracts(@AuthenticationPrincipal User userDetails) {
        try{
            Long coachId = userDetails.getCoach().getId();
            List<Contract> contracts = contractService.getContracts(coachId);
            return new ResponseEntity<>(contracts, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(new RestResponse(true,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get a Single Contract by ContractId
    @GetMapping("/{id}")
    public ResponseEntity<?> getContractById(@PathVariable("id") Long contractId,
                                             @AuthenticationPrincipal User userDetails) {
        try{
            Long coachId = userDetails.getCoach().getId();
            Contract contract = contractService.getSingleContract(coachId,contractId);
            return new ResponseEntity<>(contract, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(new RestResponse(true,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //Get contract by client Id
    @GetMapping("byClient/{id}")
    public ResponseEntity<?> getContractByClientId(@PathVariable("id") Long clientId,
                                                   @AuthenticationPrincipal User userDetails) {
        try{
            List<Contract> contract = contractService.getContractByClientId(clientId);
            return new ResponseEntity<>(contract, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //API TO GET CONTRACTS BY ORG ID
    @GetMapping(path = "getOrgContracts/{id}")
    ResponseEntity<?> getOrgContracts(@PathVariable("id") Long orgId,
                                      @AuthenticationPrincipal User userDetails){
        log.info("Request to get Organization contracts", orgId);
        try {
            List<Contract> listResponse = contractService.getContractByOrgId(orgId);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error Occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "Error Occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Create contract
    @PostMapping
    public ResponseEntity<?> createContract(
            @RequestBody ContractRequest contractRequest,
            @AuthenticationPrincipal User userDetails
            ) {
        try {
            Long coachId = userDetails.getCoach().getId();
            log.info("Request to add new Contract by Coach of id {}",coachId);

            // Later return contract DTO
            Contract contract = contractService.createContract(coachId,contractRequest);
            return new ResponseEntity<>(contract, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred! Contact Admin", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update a contract
    //    @PutMapping("/{id}")
    //    public ResponseEntity<?> updateContract(@PathVariable("id") Long contractId,
    //                                            @RequestBody ContractRequest contractRequest,
    //                                            @AuthenticationPrincipal User userDetails) {
    //        try{
    //            Long coachId = userDetails.getCoach().getId();
    //            contractService.updateContract(coachId,contractId,contractRequest);
    //            return new ResponseEntity<>(new RestResponse(false,
    //                    "Contract Updated Successfully"), HttpStatus.OK);
    //
    //        }catch (Exception e){
    //            return new ResponseEntity<>(new RestResponse(true,e.getMessage()),
    //                    HttpStatus.INTERNAL_SERVER_ERROR);
    //        }
    //    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContract(@PathVariable("id") Long contractId,
                                            @AuthenticationPrincipal User userDetails){

        try{
            Long coachId = userDetails.getCoach().getId();
            contractService.deleteContract(coachId,contractId);
            return new ResponseEntity<>(new RestResponse(false,
                    "Contract Deleted Successfully"), HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(new RestResponse(true,e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
