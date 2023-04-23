package com.natujenge.thecouch.web.rest;
import com.natujenge.thecouch.repository.CoachRepository;
import com.natujenge.thecouch.service.dto.UploadResponse;
import com.natujenge.thecouch.util.FileUtil;
import com.natujenge.thecouch.web.rest.errors.BadRequestException;
import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.OrganizationRepository;
import com.natujenge.thecouch.service.CoachService;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.request.CoachRequest;
import com.natujenge.thecouch.service.dto.CoachDTO;
import com.natujenge.thecouch.service.dto.OnBoardCoachDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping(path = "/api/coach")
@Slf4j
public class CoachResource {

    private final CoachService coachService;
    private final OrganizationRepository organizationRepository;
    private final CoachRepository coachRepository;

    public CoachResource(CoachService coachService, OrganizationRepository organizationRepository, CoachRepository coachRepository) {
        this.coachService = coachService;
        this.organizationRepository = organizationRepository;
        this.coachRepository = coachRepository;
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
    @PostMapping("/onboard")
    public ResponseEntity<CoachDTO> onBoardCoach(@RequestBody OnBoardCoachDTO onBoardCoachDTO) {
        log.info("REST Request to onboard Coach: {}", onBoardCoachDTO);

        if (onBoardCoachDTO.getCoachId() == null) {
            throw new BadRequestException("Invalid Request. Coach Id is Mandatory");
        }

        CoachDTO result = coachService.onBoard(onBoardCoachDTO);

        return ResponseEntity.ok().body(result);
    }
    @PutMapping(path ="{id}")
    ResponseEntity<CoachDTO> updateCoach(@RequestBody CoachDTO coachDTO, @PathVariable Long id){
        log.info("Request to update baker with id : {}", id);

        if (!Objects.equals(coachDTO.getId(), id)){
            throw  new BadRequestException("ID is not valid");
        }

        if (!coachRepository.existsById(id)){
            throw  new BadRequestException("Baker with id "+id+" does not exists");
        }
        coachDTO.setFullName(coachDTO.getFirstName()+" "+coachDTO.getLastName());

        CoachDTO result = coachService.save(coachDTO);
        return ResponseEntity.ok().body(result);
    }
    @PostMapping("/logo")
    public ResponseEntity<UploadResponse> uploadLogo(@RequestParam(value = "file") MultipartFile file){
        log.info("REST Request to upload coach Logo");
        return ResponseEntity.ok().body(FileUtil.uploadFile(file, log));
    }
}
