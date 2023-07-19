package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Attachment;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.AttachmentRepository;
import com.natujenge.thecouch.repository.SessionRepository;
import com.natujenge.thecouch.service.AttachmentService;
import com.natujenge.thecouch.service.SessionService;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.web.rest.request.AttachmentRequest;
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
public class AttachmentResource {
    @Autowired
    AttachmentService attachmentService;
    @Autowired
    SessionService sessionService;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    AttachmentRepository attachmentsRepository;
    @PostMapping(value = "/upload")
    public ResponseEntity<RestResponse> uploadAttachments(
            @RequestParam(name = "sessionId") Long sessionId,
            @ModelAttribute AttachmentRequest attachmentRequest) {
        log.info("REST request to upload attachments ", attachmentRequest);

        try {
            attachmentService.uploadAttachments(sessionId, attachmentRequest);
            return ResponseEntity.ok().body(new RestResponse(false, "Attachments uploaded successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RestResponse(true, e.getMessage()));
        }
    }



    // get attachment by session_id
    @GetMapping("/get-by-session-id")
    public ResponseEntity<?> getAttachmentBySessionId(
            @RequestParam(name = "sessionId", required = false) Long sessionId
    ) {
        try {
            log.debug("REST request to get attachments given session ID: {}", sessionId);

            List<Attachment> attachments = attachmentService.getAttachmentBySessionId(sessionId);
            return new ResponseEntity<>(attachments, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred", e);
            return new ResponseEntity<>(new RestResponse(true, "An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
