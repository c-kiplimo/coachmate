package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.service.OrganizationService;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(path="/api/organizations")
public class OrganizationResource {
    @Autowired
    private ModelMapper modelMapper;
    private final OrganizationService organizationService;

    public OrganizationResource(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    ResponseEntity<?> addOrganization(@RequestBody Organization organization,
                                      @AuthenticationPrincipal User userDetails) {
        log.info("Request to create a Organization", organization);

        try {
            Organization newOrganization = organizationService.addNewOrganization(organization);
            if (newOrganization != null) {
                Organization response = modelMapper.map(newOrganization, Organization.class);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new RestResponse(true,
                        "Organization not Created"), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error Occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    ResponseEntity<?> getAll(@RequestParam("per_page") int perPage,
                             @RequestParam("page") int page,
                             @AuthenticationPrincipal User userDetails) {
        try {
            log.info("Request to get all organizations");
            ListResponse listResponse = organizationService.getAllOrganizations(page, perPage);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error Occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "Error Occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "{superCoachId}/{id}")
    ResponseEntity<?> deleteOrganization(@PathVariable("id") Long id,
                                         @PathVariable("superCoachId") Long superCoachId,
                                         @AuthenticationPrincipal User userDetails) {

        try {
            organizationService.deleteOrganization(id, superCoachId);
            return new ResponseEntity<>(new RestResponse(false, "Organization Deleted Successfully"),
                    HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}