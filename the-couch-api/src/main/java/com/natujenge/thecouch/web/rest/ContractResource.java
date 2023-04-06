package com.natujenge.thecouch.web.rest;
import com.natujenge.thecouch.domain.Contract;
import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.ContractStatus;
import com.natujenge.thecouch.domain.enums.UserRole;
import com.natujenge.thecouch.service.ContractService;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.request.ContractRequest;
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
           // Long coachId = userDetails.getCoach().getId();
            Contract contract = contractService.getSingleContract(contractId);
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

    @PostMapping
    public ResponseEntity<?> createContract(
            @RequestBody ContractRequest contractRequest,
            @AuthenticationPrincipal User userDetails
    ) {
        try {
            log.info("adding contract");
            Long coachId = (userDetails.getCoach() == null) ? null : userDetails.getCoach().getId();
            log.info("coach id {}",coachId);
            Long organizationId = (userDetails.getOrganization() == null) ? null : userDetails.getOrganization().getId();
            log.info("org id {}",organizationId);

            // Later return contract DTO
            Contract contract = contractService.createContract(coachId, organizationId, contractRequest);
            return new ResponseEntity<>(contract, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping(path = "/changeContractStatus/{id}") // change status signed or finished
    ResponseEntity<?> updateContractStatus(

                                         @RequestParam("status") ContractStatus contractStatus,
                                         @PathVariable Long id,
                                         @AuthenticationPrincipal User userDetails) {
        log.info("Request to update contract status to {}", contractStatus);
            try {
                if(userDetails.getUserRole() != UserRole.CLIENT){
                    return new ResponseEntity<>(new RestResponse(true, "Only a client can sign a contract"),
                            HttpStatus.INTERNAL_SERVER_ERROR);
                }
                contractService.updateContractStatusByClientId
                        (id,contractStatus, userDetails.getId());

            return new ResponseEntity<>(new RestResponse(false, "Contract status set to "+ contractStatus),
                    HttpStatus.OK);


        } catch (Exception e){
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
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



    @PostMapping("/organization-coach-contract")
    public ResponseEntity<?> createOrganizationAndCoachContract(
            @RequestBody ContractRequest contractRequest,
            @AuthenticationPrincipal User userDetails
    ) {
        try {
            Long organizationId = userDetails.getOrganization().getId();
            log.info("Request to add new Contract by Organization of id {}",organizationId);

            // Later return contract DTO
            Contract contract = contractService.createOrganizationAndCoachContract(organizationId,contractRequest);
            return new ResponseEntity<>(contract, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/organization-client-contract")
    public ResponseEntity<?> createOrganizationAndClientContract(
            @RequestBody ContractRequest contractRequest,
            @AuthenticationPrincipal User userDetails
    ) {
        try {
            Long organizationId = userDetails.getOrganization().getId();
            Long coachId = userDetails.getCoach().getId();
            log.info("Request to add new Contract by Organization of id {}",organizationId);

            // Later return contract DTO
            Contract contract = contractService.createContract(coachId,organizationId,contractRequest);
            return new ResponseEntity<>(contract, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
