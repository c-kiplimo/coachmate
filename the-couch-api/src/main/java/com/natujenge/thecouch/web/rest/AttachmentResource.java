package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Attachments;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.AttachmentsRepository;
import com.natujenge.thecouch.repository.SessionRepository;
import com.natujenge.thecouch.service.AttachmentService;
import com.natujenge.thecouch.service.SessionService;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
@Slf4j
public class AttachmentResource {
    @Autowired
    AttachmentService attachmentService;
    @Autowired
    SessionService sessionService;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    AttachmentsRepository attachmentsRepository;
    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadAttachments(
            @RequestParam(name = "sessionId", required = false) Long sessionId,
            @RequestParam(name = "files", required = false) MultipartFile[] files,
            @RequestParam(name = "links", required = false) String[] links) {
        try {
            attachmentService.uploadAttachments(sessionId, files, links);
            return new ResponseEntity<>(new RestResponse(false, "Attachments uploaded successfully"), HttpStatus.OK);
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
                List<Attachments> attachments = attachmentService.getAttachmentBySessionId(
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
