package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Attachments;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.AttachmentsRepository;
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
    public ResponseEntity<RestResponse> uploadAttachments(
            @RequestParam(name = "sessionId") Long sessionId,
            @RequestBody List<AttachmentRequest> attachmentRequests) {

        try {
            attachmentService.uploadAttachments(sessionId, attachmentRequests);
            return ResponseEntity.ok().body(new RestResponse(false, "Attachments uploaded successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RestResponse(true, e.getMessage()));
        }
    }



    // get attachment by session_id
    @GetMapping("/get-by-session-id")
    public ResponseEntity<?> getAttachmentBySessionId(
            @AuthenticationPrincipal User userDetails,
            @RequestParam(name = "sessionId", required = false) Long sessionId
    ) {
        try {
            log.debug("REST request to get attachments given session ID: {}", sessionId);

            List<Attachments> attachments = attachmentService.getAttachmentBySessionId(sessionId);
            return new ResponseEntity<>(attachments, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred", e);
            return new ResponseEntity<>(new RestResponse(true, "An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
