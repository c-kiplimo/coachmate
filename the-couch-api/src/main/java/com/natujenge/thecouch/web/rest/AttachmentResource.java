package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Attachment;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<Long>> uploadAttachments(
            //accept an array of attachment requests
            @RequestBody List<AttachmentRequest> attachmentRequest,
            @RequestParam(name = "sessionId", required = false) Long sessionId
    ) {
        log.info("REST request to upload attachments for session ID: {}", sessionId);

        try {
            List<Attachment> createdAttachments = new ArrayList<>();
            for (AttachmentRequest attachment : attachmentRequest) {
                Attachment newAttachment = attachmentService.uploadAttachments(sessionId, attachment);
                createdAttachments.add(newAttachment);
            }

            // Extract the IDs of the created attachments
            List<Long> attachmentIds = createdAttachments.stream()
                    .map(Attachment::getId)
                    .collect(Collectors.toList());

            return ResponseEntity.created(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .build().toUri()
            ).body(attachmentIds);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // get attachment by session_id
    @GetMapping("/get-by-session-id")
    public ResponseEntity<?> getAttachmentBySessionId(
            @RequestParam(name = "sessionId", required = false) Long sessionId
    ) {
        try {
            log.debug("REST request to get attachments given session ID: {}", sessionId);

            List<Attachment> attachments = attachmentService.getAttachmentBySession(sessionId);
            return new ResponseEntity<>(attachments, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred", e);
            return new ResponseEntity<>(new RestResponse(true, "An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //delete attachment by id
    @DeleteMapping("/delete-by-id/{attachmentId}")
    public ResponseEntity<?> deleteAttachmentById(
            @PathVariable(name = "attachmentId", required = false) Long attachmentId
    ) {
        try {
            log.debug("REST request to delete attachment given attachment ID: {}", attachmentId);

            attachmentService.deleteAttachmentById(attachmentId);
            return new ResponseEntity<>(new RestResponse(false, "Attachment deleted successfully"), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred", e);
            return new ResponseEntity<>(new RestResponse(true, "An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
