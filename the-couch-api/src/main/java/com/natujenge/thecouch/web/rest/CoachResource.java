package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.OrganizationRepository;
import com.natujenge.thecouch.service.CoachService;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.request.CoachRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/coach")
@Slf4j
public class CoachResource {

    private final CoachService coachService;
    private final OrganizationRepository organizationRepository;

    public CoachResource(CoachService coachService, OrganizationRepository organizationRepository) {
        this.coachService = coachService;
        this.organizationRepository = organizationRepository;
    }



    //api to create a coach by organization
    @PostMapping
    ResponseEntity<?> addNewCoach(@RequestBody CoachRequest coachRequest,
                                  @AuthenticationPrincipal User userDetails) {

        try {
            Organization organization = userDetails.getOrganization();
            log.info("request to add new coach by organization{}",organization.getId());
            coachService.addNewCoachByOrganization(organization, userDetails.getMsisdn(),
                    coachRequest);

            return new ResponseEntity<>(new RestResponse(false,
                    "Coach by organization  created"), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
