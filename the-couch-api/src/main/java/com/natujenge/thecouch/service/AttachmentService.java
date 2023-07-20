package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Attachment;
import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.repository.AttachmentRepository;
import com.natujenge.thecouch.repository.OrganizationRepository;
import com.natujenge.thecouch.repository.SessionRepository;
import com.natujenge.thecouch.web.rest.request.AttachmentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    public List<Attachment> getAttachmentBySession(Long sessionId) {
        log.info("Request to get attachments by session ID: {}", sessionId);
        //get session by id
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));

        return attachmentRepository.findBySession(session);
    }


    public Attachment uploadAttachments(Long sessionId, AttachmentRequest attachmentRequest) {
        log.info("Request to upload attachments for session ID: {}", sessionId);

            Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));
            //save one attachment
            Attachment attachment = new Attachment();
            attachment.setSession(session);
            attachment.setLink(attachmentRequest.getLink());
            attachment.setCreatedAt(LocalDateTime.now());
            attachment.setLastUpdatedAt(LocalDateTime.now());
            return attachmentRepository.save(attachment);

    }

    public void deleteAttachmentById(Long attachmentId) {
        log.info("Request to delete attachment by ID: {}", attachmentId);
        //find attachment by id
        Attachment attachment = attachmentRepository.findById(attachmentId).orElseThrow(() -> new RuntimeException("Attachment not found"));
        //delete attachment
        if (attachment != null) {
            attachmentRepository.delete(attachment);
        }
    }
}




