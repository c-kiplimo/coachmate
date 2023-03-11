package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Attachments;
import com.natujenge.thecouch.service.AttachmentsService;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
@Slf4j
public class AttachmentsResource {
    @Autowired
    AttachmentsService attachmentsService;
    @PostMapping
    ResponseEntity<?> createNewAttachment(@RequestBody Attachments attachments,
                                          @RequestParam(name = "sessionId",required = false) Long sessionId,
                                          @RequestParam(name = "coachId") Long coachId,

                                          @RequestParam(name = "orgIdId", required = false) Long orgIdId,

                                          @AuthenticationPrincipal User userDetails) {
        log.info("request to create feedback");
        try {
            attachmentsService.createNewAttachment(sessionId, coachId, orgIdId, attachments);
            return new ResponseEntity<>(new RestResponse(false,"Attachment Received Successfully"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // get attachment by session_id
    @GetMapping("/get-by-session-id")
    public ResponseEntity<?> getAttachmentBySessionId(
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "sessionId",required = false) Long sessionId
    ) {
        try {

            log.debug(
                    "REST request to get attachment given session Id {} and coachId {}",
                    sessionId
            );
            try {
                List<Attachments> attachments = attachmentsService.getAttachmentBySessionId(
                        sessionId
                );
                return new ResponseEntity<>(attachments, HttpStatus.OK);
            } catch (Exception e) {
                log.error("Error Occurred ", e);
                return new ResponseEntity<>(new RestResponse(true, "Error Occurred"),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            log.error("Error occurred ", e);
            return new ResponseEntity<>(new RestResponse(true, "An Error occurred, contact admin"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
