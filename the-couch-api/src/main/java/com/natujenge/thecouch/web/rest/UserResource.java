package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.ClientStatus;
import com.natujenge.thecouch.domain.enums.SessionStatus;
import com.natujenge.thecouch.domain.enums.UserRole;
import com.natujenge.thecouch.exception.BadRequestException;
import com.natujenge.thecouch.repository.UserRepository;
import com.natujenge.thecouch.service.RegistrationService;
import com.natujenge.thecouch.service.UserService;
import com.natujenge.thecouch.util.FileUtil;
import com.natujenge.thecouch.util.PaginationUtil;
import com.natujenge.thecouch.web.rest.dto.*;
import com.natujenge.thecouch.web.rest.request.ClientRequest;
import com.natujenge.thecouch.web.rest.request.CoachRequest;

import com.natujenge.thecouch.web.rest.request.RegistrationRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(path = "/api/users")
public class UserResource {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final RegistrationService registrationService;


    public UserResource(ModelMapper modelMapper, UserService userService, RegistrationService registrationService,
                        UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.registrationService = registrationService;
        this.userRepository = userRepository;
    }

    //api to create a coach by organization
    @PostMapping(path = "/coach")
    ResponseEntity<?> addNewCoach(@RequestBody RegistrationRequest registrationRequest,
                                  @AuthenticationPrincipal User userDetails) {

        try {
            Organization organization = userDetails.getOrganization();
            log.info("request to add new coach by organization{}", organization.getId());
            registrationService.addCoach(organization, registrationRequest);

            return new ResponseEntity<>(new RestResponse(false,
                    "Coach by organization  created"), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //api to create a client user
    @PostMapping
    ResponseEntity<?> addNewClient(@RequestBody ClientRequest clientRequest,
                                   @AuthenticationPrincipal User userDetails) {
        log.info("request to add new client");
        try {
            Optional<Organization> organization = Optional.ofNullable(userDetails.getOrganization());

            User newClientUser;
            if (organization.isPresent()) {
                newClientUser = userService.addNewClient(userDetails,
                        organization.get(),
                        clientRequest);
            } else {
                newClientUser = userService.addNewClient(userDetails,
                        null, clientRequest);
            }

            if (newClientUser != null) {
                ClientRequest response = modelMapper.map(newClientUser, ClientRequest.class);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new RestResponse(true,
                        "Client not created"), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET CLIENTS By ORG ID or COACH ID
    @GetMapping(path = "clients")
    ResponseEntity<List<ClientDTO>> getClients(@RequestParam(name = "orgId", required = false) Long orgId,
                                               @RequestParam(name = "coachId", required = false) Long coachId,
                                               @RequestParam(name = "status", required = false) String status,
                                               @RequestParam(name = "search", required = false) String search,
                                               Pageable pageable,
                                               @AuthenticationPrincipal User userDetails) {
        log.info("Request to get clients {}, ", coachId);
        Long organizationId = null;
        if (userDetails.getOrganization() != null) {
            organizationId = userDetails.getOrganization().getId();
        }
        Page<ClientDTO> clientDtoPage = userService.getClients(coachId, status, search, organizationId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), clientDtoPage);
        return ResponseEntity.ok().headers(headers).body(clientDtoPage.getContent());
    }


    //GET CLIENT BY ID
    @GetMapping(path = "client/{id}")
    ResponseEntity<?> getClientById(@PathVariable("id") Long clientId,
                                    @AuthenticationPrincipal User userDetails) {
        log.info("Request to get client {}", clientId);
        try {
            User client = userService.getClientById(clientId, userDetails);
            if (client != null) {
                ClientDTO response = modelMapper.map(client, ClientDTO.class);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new RestResponse(true,
                        "Client not found"), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //API TO GET CLIENTS BY ORG ID
    @GetMapping(path = "getOrgClients/{id}")
    ResponseEntity<?> getOrgClients(@PathVariable("id") Long orgId,
                                    @AuthenticationPrincipal User userDetails) {
        log.info("Request to get Organization clients");
        List<User> listResponse;
        try {
            listResponse = userService.getClientByOrgId(orgId, UserRole.CLIENT);
        } catch (Exception e) {
            log.error("Error Occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "Error Occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(listResponse, HttpStatus.OK);
    }

    @GetMapping(path = "coaches")
    ResponseEntity<List<CoachDTO>> getCoaches(
            @RequestParam(name = "orgId", required = false) Long organizationId,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "search", required = false) String search,
            Pageable pageable,
            @AuthenticationPrincipal User userDetails
    ) {
        log.info("Request to get coaches");
        if (userDetails != null && userDetails.getOrganization() != null) {
            organizationId = userDetails.getOrganization().getId();
        } else {
            // Return an appropriate response or throw an exception for unauthorized access
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Only return coaches created by the logged in user's organization
        Page<CoachDTO> coachDtoPage = userService.getCoaches(status, search, organizationId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), coachDtoPage);
        return ResponseEntity.ok().headers(headers).body(coachDtoPage.getContent());
    }



    @GetMapping(path = "coach/{id}")
    ResponseEntity<?> getCoachById(@PathVariable("id") Long coachId,
                                   @AuthenticationPrincipal User userDetails) {
        log.info("Request to get client {}", coachId);
        try {
            User coach = userService.getCoachById(coachId, userDetails);
            if (coach != null) {
                CoachDTO response = modelMapper.map(coach, CoachDTO.class);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new RestResponse(true,
                        "Client not found"), HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/change-status/{id}")
        // change status active or suspend
    ResponseEntity<ClientDTO> updateClientStatus(@RequestParam("status") ClientStatus clientStatus,
                                                 @PathVariable("id") Long id,
                                                 @AuthenticationPrincipal User userDetails) {

        log.info("request to change client status with id : {} to status {} by user with id {}", id, clientStatus, userDetails.getId());
        ClientDTO clientDTO = userService.updateClientStatus(id, clientStatus);
        return new ResponseEntity<>(clientDTO, HttpStatus.OK);

    }

    //save google auth data to user, the data is the same as the one returned by google after successful login
    @PostMapping(path = "/google-auth")
    ResponseEntity<?> saveGoogleAuthData(@RequestBody String googleAuthData,
                                         @AuthenticationPrincipal User userDetails) {
        log.info("request to save google auth data for user with id {}", userDetails.getId());
        userService.saveGoogleAuthData(googleAuthData, userDetails);
        return new ResponseEntity<>(new RestResponse(false, "Google auth data saved successfully"), HttpStatus.OK);
    }

    //Remove google auth data from user
    @DeleteMapping(path = "/google-auth")
    ResponseEntity<?> removeGoogleAuthData(@AuthenticationPrincipal User userDetails) {
        log.info("request to remove google auth data for user with id {}", userDetails.getId());
        userService.removeGoogleAuthData(userDetails);
        return new ResponseEntity<>(new RestResponse(false, "Google auth data removed successfully"), HttpStatus.OK);
    }

    //add calendlyUsername to user
    @PostMapping(path = "/calendly-username")
    ResponseEntity<?> addCalendlyUsername(@RequestBody String calendlyUsername,
                                          @AuthenticationPrincipal User userDetails) {
        log.info("request to add calendly username for user with id {}", userDetails.getId());
        userService.addCalendlyUsername(calendlyUsername, userDetails);
        return new ResponseEntity<>(new RestResponse(false, "Calendly username added successfully"), HttpStatus.OK);
    }

    //Get who added a client user
    @GetMapping(path = "/added-by/{id}")
    ResponseEntity<?> getAddedBy(@PathVariable("id") Long id,
                                  @AuthenticationPrincipal User userDetails) {
        log.info("request to get who added a client user with id {}", id);
        User user = userService.getAddedBy(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/logo")
    public ResponseEntity<UploadResponse> uploadLogo(@RequestParam(value = "file") MultipartFile file){
        log.info("REST Request to upload user Logo");
        return ResponseEntity.ok().body(FileUtil.uploadFile(file, log));
    }
    @PutMapping(path = "organization/{id}")
    ResponseEntity<OrganizationDTO> updateOrganization(@RequestBody OrganizationDTO organizationDTO,
                                                       @PathVariable Long id) throws BadRequestException {
        log.info("Request to update organization with id : {}", id);

        if (!Objects.equals(organizationDTO.getId(), id)) {
            throw new BadRequestException("ID is not valid");
        }

        if (!userRepository.existsById(id)) {
            throw new BadRequestException("Organization with id " + id + " does not exists");
        }
        organizationDTO.setFullName(organizationDTO.getFirstName() + " " + organizationDTO.getLastName());

        OrganizationDTO result = userService.saveOrganization(organizationDTO);
        return ResponseEntity.ok().body(result);

    }
    @PutMapping(path="coaches/{id}")
    ResponseEntity<CoachDTO> updateCoach(@RequestBody CoachDTO coachDTO,
                                         @PathVariable Long id) throws BadRequestException {
        log.info("Request to update coach with id : {}", id);

        if (!Objects.equals(coachDTO.getId(), id)) {
            throw new BadRequestException("ID is not valid");
        }

        if (!userRepository.existsById(id)) {
            throw new BadRequestException("Coach with id " + id + " does not exists");
        }
        coachDTO.setFullName(coachDTO.getFirstName() + " " + coachDTO.getLastName());

        CoachDTO result = userService.saveCoach(coachDTO);
        return ResponseEntity.ok().body(result);
    }
    @PutMapping(path="clients/{id}")
    ResponseEntity<ClientDTO> updateClient(@RequestBody ClientDTO clientDTO,
                                           @PathVariable Long id) throws BadRequestException {
        log.info("Request to update client with id : {}", id);

        if (!Objects.equals(clientDTO.getId(), id)) {
            throw new BadRequestException("ID is not valid");
        }

        if (!userRepository.existsById(id)) {
            throw new BadRequestException("Client with id " + id + " does not exists");
        }
       clientDTO.setFullName(clientDTO.getFirstName() + " " + clientDTO.getLastName());
        ClientDTO result = userService.saveClient(clientDTO);
        return ResponseEntity.ok().body(result);
    }
}