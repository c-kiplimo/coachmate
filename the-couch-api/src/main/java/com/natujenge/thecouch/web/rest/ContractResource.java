package com.natujenge.thecouch.web.rest;
import com.natujenge.thecouch.domain.Contract;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.ContractStatus;
import com.natujenge.thecouch.domain.enums.UserRole;
import com.natujenge.thecouch.service.ContractService;
import com.natujenge.thecouch.service.dto.ContractDTO;
import com.natujenge.thecouch.util.PaginationUtil;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.request.ContractRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@RestController
@Slf4j

@RequestMapping(path = "api/contracts")
public class ContractResource {

  private final ContractService contractService;

    public ContractResource(ContractService contractService) {
        this.contractService = contractService;
    }

    // Get All Contracts
    @GetMapping
    public ResponseEntity<?> getContracts(@AuthenticationPrincipal User userDetails) {
        try{
            Long coachId = userDetails.getId();
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
            Contract contract = null;

            if (userDetails.getUserRole() == UserRole.COACH) {
                Long coachId = userDetails.getId();
                log.info("coach id {}", coachId);
                contract = contractService.createContract(coachId, contractRequest);
            } else if (userDetails.getOrganization() != null) {
                Long organizationId = userDetails.getOrganization().getId();
                log.info("org id {}", organizationId);
                if (Objects.equals(contractRequest.getCoachId(), null)) {
                    contract = contractService.createOrganizationAndCoachContract(organizationId, contractRequest);
                    return new ResponseEntity<>(contract, HttpStatus.CREATED);
                } else {
                    contract = contractService.createOrganizationAndClientContract(organizationId, contractRequest);
                    return new ResponseEntity<>(contract, HttpStatus.CREATED);
                }
            }

            if (contract == null) {
                return new ResponseEntity<>("Unable to determine user role", HttpStatus.BAD_REQUEST);
            }

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
            Long coachId = userDetails.getId();
            contractService.deleteContract(coachId,contractId);
            return new ResponseEntity<>(new RestResponse(false,
                    "Contract Deleted Successfully"), HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(new RestResponse(true,e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter")
    ResponseEntity<List<ContractDTO>> filterFarmers(@RequestParam(name = "client_id", required = false) Long clientId,

                                                    @RequestParam(name = "search", required = false) String search,
                                                    Pageable pageable,
                                                    @AuthenticationPrincipal User userDetails) {
        Long organisationId=null;
        if(userDetails.getOrganization() !=null){
            organisationId = userDetails.getOrganization().getId();

        }
        UserRole userRole = userDetails.getUserRole();
        Long userId= userDetails.getId();

        Page<ContractDTO> contractPage = contractService.filter(userId,clientId , userRole,search, organisationId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), contractPage);
        return ResponseEntity.ok().headers(headers).body(contractPage.getContent());
    }
}
