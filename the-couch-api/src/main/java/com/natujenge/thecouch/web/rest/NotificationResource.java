package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.natujenge.thecouch.service.NotificationService;
import com.natujenge.thecouch.web.rest.request.NotificationRequest;
import com.natujenge.thecouch.web.rest.dto.ListResponse;

@RestController
@RequestMapping("/api/notification")
@Slf4j
public class NotificationResource {

    @Autowired
    private ModelMapper modelMapper;
    private final NotificationService notificationService;

    public NotificationResource(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    ResponseEntity<?> getAllNotifications(@RequestParam("per_page") int perPage,
                                          @RequestParam("page") int page,
                                          @AuthenticationPrincipal User userDetails){
        /*
         * Get All Notifications by coachId
         */

        try{
            Long coachId = userDetails.getId();
            log.info("Requesting all notifications by coach with id {}",coachId);

            return new ResponseEntity<>(notificationService.getAllNotifications(page, perPage,coachId), HttpStatus.OK);
        }catch (Exception e){
            log.error("Error ", e);
            return new ResponseEntity<>(new RestResponse(true, "Error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(@PathVariable(name = "id") Long id,
                                                 @AuthenticationPrincipal User userDetails) {

        /*
         * Get notification by notificationId
         */
        try{
            Long coachId = userDetails.getId();
            log.info("Requesting notification of id {} by coach of id {}", id,coachId);
            return new ResponseEntity<>(notificationService.getNotificationById(id,coachId),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new RestResponse(true,"Error Occurred! Contact Admin"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody NotificationRequest notificationRequest,
                                                @AuthenticationPrincipal User userDetails) {

        try{
            long coachId = userDetails.getId();
            log.info("Request to add new notification by coach with id {}",coachId);

            // convert DTO to entity
            notificationService.createNotificationByCoachId(notificationRequest,coachId);
            return new ResponseEntity<>(new RestResponse(false,"Notification added successfully"), HttpStatus.CREATED);
        } catch(Exception e){
            return new ResponseEntity<>(new RestResponse(true,"Error Occurred! Contact Admin"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Get Notifications by client id and coach id order by desc
    @GetMapping("/filter-by-client-id and-coach-id")
    public ResponseEntity<?> filterNotificationsByClientIdAndCoachId(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "client_id",required = false) Long clientId
    ) {
        try {
            Long coachId = userDetails.getId();
            log.debug(
                    "REST request to filter notifications given, coach id : {}, client id  : {}",
                    clientId,
                    coachId

            );

            ListResponse notifications = (ListResponse) notificationService.filterByClientIdAndCoachId(
                    page,
                    perPage,
                    clientId,
                    coachId
            );
            //LATER add HTTP headers
            return ResponseEntity.ok().body(notifications);
        } catch (Exception e){
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "An Error occurred, contact admin"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // Filter notifications by sessionId
    @GetMapping("/filter-by-session-id")
    public ResponseEntity<?> filterNotificationsBySessionIdAndCoachId(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "session_id",required = false) Long sessionId
    ) {
        try {
            Long coachId = userDetails.getId();
            log.debug(
                    "REST request to filter notifications given, coach id : {}",
                    coachId
            );
            log.info("Coach id {}, session id {}",coachId,sessionId);
            ListResponse notifications = notificationService.filterBySessionIdAndCoachId(
                    page,
                    perPage,
                    sessionId,
                    coachId
            );
            //LATER add HTTP headers
            return ResponseEntity.ok().body(notifications);
        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "An Error occurred, contact admin"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Filter notifications by clientId
    @GetMapping("/filter-by-client-id")
    public ResponseEntity<?> filterNotificationsByClientId(
            @RequestParam("per_page") int perPage,
            @RequestParam("page") int page,
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "client_id",required = false) Long clientId) {
        try {
            log.info("client id {}",clientId);
            ListResponse notifications = notificationService.filterByClientId(
                    page,
                    perPage,
                    clientId
            );
            //LATER add HTTP headers
            return ResponseEntity.ok().body(notifications);
        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "An Error occurred, contact admin"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
