package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.Attachments;
import com.natujenge.thecouch.repository.SessionRepository;
import com.natujenge.thecouch.service.AttachmentsService;
import com.natujenge.thecouch.repository.AttachmentsRepository;
import com.natujenge.thecouch.service.SessionService;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
@Slf4j
public class AttachmentsResource {
    @Autowired
    AttachmentsService attachmentsService;
    @Autowired
    SessionService sessionService;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    AttachmentsRepository attachmentsRepository;
    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadAttachments( @RequestParam(name = "sessionId",required = false) Long sessionId,
                                               @RequestParam(name ="files",required = true) MultipartFile  files,
                                               @RequestParam(name ="links",required = false) String[] links) {
        try {
            // Get the session associated with the given sessionId
            Session session = sessionRepository.findById(sessionId).orElseThrow(
                    () -> new Exception("Session not found")
            );
            log.info("Files found {}", files);
            log.info("SessionId found {}", sessionId);

            // Handle file uploads
            if (files != null){
//                for (MultipartFile file : files) {
                    // Create a new attachment entity for each file and associate it with the session
                    Attachments attachment = new Attachments();
                    attachment.setSession(session);
                    attachment.setFileName(files.getOriginalFilename());
                    attachment.setFileType(files.getContentType());
                    attachment.setFileSize(files.getSize());
                    attachment.setFileContent(files.getBytes());
                // attachment Number Generation
                int randNo = (int) ((Math.random() * (99999 - 1)) + 1);
                String attachmentL = String.format("%05d", randNo);
                String attachmentNo = attachment.getCoach().getBusinessName().substring(0, 2) +
                        attachment.getClient().getFirstName().charAt(0) + attachment.getSession().getName().charAt(0) + "-" + attachmentL;
                attachment.setAttachmentNumber(attachmentNo);
                    attachmentsRepository.save(attachment);
//                }

            }
            if (links != null){
                for (String link : links) {
                    // Create a new attachment entity for each link and associate it with the session
                    Attachments attachment = new Attachments();
                    attachment.setSession(session);
                    attachment.setFileName(link);
                    attachment.setFileType("link");
                    attachment.setLinkUrl(link);
                    attachmentsRepository.save(attachment);
                }
            }

            return new ResponseEntity<>(new RestResponse(false, "Attachments uploaded successfully"),
                    HttpStatus.OK);
        } catch (Exception e){
            log.error("Error occurred while uploading attachments ", e);
            return new ResponseEntity<>(new RestResponse(true, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
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
