package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Contract;
import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.ContractStatus;
import com.natujenge.thecouch.domain.enums.UserRole;
import com.natujenge.thecouch.service.ContractService;
import com.natujenge.thecouch.util.PaginationUtil;
import com.natujenge.thecouch.web.rest.dto.ContractDTO;
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

@RestController
@Slf4j
@RequestMapping(path = "api/contracts")
public class ContractResource {

  private final ContractService contractService;

    public ContractResource(ContractService contractService) {
        this.contractService = contractService;
    }

    // Get a Single Contract by ContractId
    @GetMapping("/{id}")
    public ResponseEntity<?> getContractById(@PathVariable("id") Long contractId,
                                             @AuthenticationPrincipal User userDetails) {
        try{
           // Long coachId = userDetails.getCoach().getId();
            Contract contract = contractService.findContractById(contractId);
            return new ResponseEntity<>(contract, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(new RestResponse(true,e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //API TO GET CONTRACTS BY ORG ID
    @PostMapping
    public ResponseEntity<Contract> createContract(
            @RequestBody ContractRequest contractRequest,
            @AuthenticationPrincipal User userDetails
    ) {
        log.info("Request to create contract");
        try {
            Long organisationId=null;
            if(userDetails.getOrganization() !=null){
                organisationId = userDetails.getOrganization().getId();
            }
            Contract contract = contractService.createContract(userDetails.getId(), contractRequest, organisationId);
            return ResponseEntity.created(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(contract.getId())
                            .toUri())
                    .body(contract);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }




    @PutMapping(path = "/changeContractStatus/{id}") // change status signed or finished
    ResponseEntity<Contract> updateContractStatus(
                                         @RequestParam("status") ContractStatus contractStatus,
                                         @PathVariable("id") Long contractId,
                                         @AuthenticationPrincipal User userDetails) {
        log.info("Request to update contract status to {}", contractStatus);

                Contract updatedContract=contractService.updateContractStatus(contractId,contractStatus, userDetails.getId());
        return  ResponseEntity.ok().body(updatedContract);
    }

    // update contract
    @PutMapping(path = "/{id}")
    public ResponseEntity<ContractDTO> updateContract(@PathVariable("id") Long contractId,
                                                      @RequestBody Contract contract,
                                                      @AuthenticationPrincipal User userDetails){
        log.info("Request to update contract with id {}", contractId);
        try {
            ContractDTO updatedContract = contractService.updateContract(contractId, contract);
            return ResponseEntity.ok().body(updatedContract);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


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
    ResponseEntity<List<ContractDTO>> filterContracts(@RequestParam(name = "clientId", required = false) Long clientId,
                                                    @RequestParam(name = "coachId", required = false) Long coachId,
                                                    @RequestParam(name = "search", required = false) String search,
                                                    @RequestParam(name = "status", required = false) ContractStatus status,
                                                    @RequestParam(name="organisationId", required = false) Long organisationId,
                                                    Pageable pageable,
                                                    @AuthenticationPrincipal User userDetails) {

        Page<ContractDTO> contractPage = contractService.filter(coachId, clientId, search, status, organisationId, pageable);
        log.info("filtered {}",contractPage.getContent());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), contractPage);
        return ResponseEntity.ok().headers(headers).body(contractPage.getContent());
    }
}
