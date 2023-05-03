package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.web.rest.request.NotificationSettingsRequest;
import com.natujenge.thecouch.service.NotificationSettingsService;
import com.natujenge.thecouch.domain.*;
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
@RequestMapping("/api/notification-settings")
public class NotificationSettingsResource {
    @Autowired
    private ModelMapper modelMapper;
    private final NotificationSettingsService notificationSettingsService;

    public NotificationSettingsResource(NotificationSettingsService notificationSettingsService) {
        this.notificationSettingsService = notificationSettingsService;
    }


    @PutMapping
    public ResponseEntity<?> updateNotificationSettings(
            @RequestBody NotificationSettingsRequest notificationSettingsRequest,
            @AuthenticationPrincipal User userDetails) {

        try{
            Long coachId = userDetails.getId();
            String coachName = userDetails.getFullName();
            log.info("Request to update notification settings by coach with id {}",coachId);


            NotificationSettings notificationSettings = notificationSettingsService.updateSettings
                    (notificationSettingsRequest,coachId,coachName);
            // map notification settings to the NotificationRequest and return object.

            NotificationSettingsRequest response = modelMapper.map(notificationSettings,
                    NotificationSettingsRequest.class);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch(Exception e){
            return new ResponseEntity<>(new RestResponse(true,"Error Occurred while updating settings"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    ResponseEntity<?> addNewNotification(@RequestBody NotificationSettingsRequest notificationSettingsRequest,
                                         @AuthenticationPrincipal User userDetails) {
        log.info("request to create notification");
        try{
            Long coachId = userDetails.getId();
            String coachName = userDetails.getFullName();
            log.info("Request to create notification settings by coach with id {}",coachId);


            notificationSettingsService.addNewSettings(notificationSettingsRequest);

            return new ResponseEntity<>(new RestResponse(false,"Settings added successfully"),
                    HttpStatus.CREATED);
        } catch(Exception e){
            return new ResponseEntity<>(new RestResponse(true,"Error Occurred! when adding settings"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
