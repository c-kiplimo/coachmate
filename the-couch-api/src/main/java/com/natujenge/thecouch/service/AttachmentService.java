package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Attachment;
import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.repository.AttachmentRepository;
import com.natujenge.thecouch.repository.OrganizationRepository;
import com.natujenge.thecouch.repository.SessionRepository;
import com.natujenge.thecouch.web.rest.request.AttachmentRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class AttachmentService {

    private final SessionRepository sessionRepository;

    private final AttachmentRepository attachmentRepository;


    private  final UserService userService;


    private final OrganizationRepository organizationRepository;

    public AttachmentService(SessionRepository sessionRepository,UserService userService, AttachmentRepository attachmentRepository, OrganizationRepository organizationRepository) {
        this.sessionRepository = sessionRepository;
        this.attachmentRepository = attachmentRepository;
        this.userService=userService;

        this.organizationRepository = organizationRepository;
    }

    //get attachment by session id
    public List<Attachment> getAttachmentBySessionId(Long sessionId) {
        log.info("Request to get attachments by session ID: {}", sessionId);

        List<Attachment> attachments = attachmentRepository.findBySessionId(sessionId);

        return attachments;
    }


    public void uploadAttachments(Long sessionId, AttachmentRequest attachmentRequest) {
        log.info("Request to upload attachments for session ID: {}", sessionId);

        try {
            Session session = sessionRepository.findById(sessionId)
                    .orElseThrow(() -> new Exception("Session not found"));

            processAttachment(session, attachmentRequest);
        } catch (Exception e) {
            log.error("Error while uploading attachments for session ID: {}", sessionId, e);
        }
    }

    private void processAttachment(Session session, AttachmentRequest attachmentRequest) {
        log.info("Processing attachments for session ID: {}", session.getId());

        try {
            List<String> links = attachmentRequest.getLinks();


            if (links != null && !links.isEmpty()) {
                for (String link : links) {
                    Attachment attachment = new Attachment();
                    attachment.setSession(session);
                    attachment.setLinks(List.of(link));
                    attachment.setCreatedAt(LocalDateTime.now());
                    setAttachmentNumber(attachment);
                    // Set other fields as needed...
                    attachmentRepository.save(attachment);

                }
            }

            log.info("Attachments uploaded successfully for session ID: {}", session.getId());

        } catch (Exception e) {
            log.error("Error while processing attachments for session ID: {}", session.getId(), e);
        }
    }

    // Generate a unique attachment number using a random UUID and taking the last 8 characters
    private void setAttachmentNumber(Attachment attachment) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            int index = new Random().nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        attachment.setAttachmentNumber(sb.toString());
    }
}




