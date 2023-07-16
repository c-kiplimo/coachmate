package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Attachments;
import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.repository.AttachmentsRepository;
import com.natujenge.thecouch.repository.OrganizationRepository;
import com.natujenge.thecouch.repository.SessionRepository;
import com.natujenge.thecouch.web.rest.request.AttachmentRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AttachmentService {

    private final SessionRepository sessionRepository;

    private final AttachmentsRepository attachmentsRepository;


    private final OrganizationRepository organizationRepository;

    public AttachmentService(SessionRepository sessionRepository, AttachmentsRepository attachmentsRepository, OrganizationRepository organizationRepository) {
        this.sessionRepository = sessionRepository;
        this.attachmentsRepository = attachmentsRepository;

        this.organizationRepository = organizationRepository;
    }

    //get attachment by session id
    public List<Attachments> getAttachmentBySessionId(Long sessionId) {
        log.info("Request to get attachments by session ID: {}", sessionId);

        List<Attachments> attachments = attachmentsRepository.findBySessionId(sessionId);

        return attachments;
    }


    public void uploadAttachments(Long sessionId, List<AttachmentRequest> attachmentRequests) throws Exception {
        log.info("Request to upload attachments for session ID: {}", sessionId);

        // Get the session associated with the given sessionId
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new Exception("Session not found"));

        if (attachmentRequests != null && !attachmentRequests.isEmpty()) {
            log.info("Processing attachments...");
            for (AttachmentRequest attachmentRequest : attachmentRequests) {
                if (attachmentRequest.getFileDownloadUri() != null) {
                    // It's a file
                    processFileAttachment(session, attachmentRequest);
                } else {
                    // It's a link
                    processLinkAttachment(session, attachmentRequest);
                }
            }

            log.info("Attachments processed successfully.");
        } else {
            log.info("No attachments to upload.");
        }
    }

    private void processFileAttachment(Session session, AttachmentRequest attachmentRequest) {
        log.info("Processing file: {}", attachmentRequest.getFileName());

        // Check if the file is a valid type (PDF)
        if ("application/pdf".equals(attachmentRequest.getFileType())) {
            log.info("File type is PDF. Uploading...");

            // Create a new attachment entity for the file and associate it with the session
            Attachments attachment = new Attachments();
            attachment.setSession(session);
            attachment.setFileName(attachmentRequest.getFileName());
            attachment.setFileType(attachmentRequest.getFileType());
            attachment.setFileSize(attachmentRequest.getSize());

            String attachmentNumber = generateAttachmentNumber("F");
            attachment.setAttachmentNumber(attachmentNumber);

            // Set other fields as needed.
            attachment.setCreatedAt(LocalDateTime.now());
            attachment.setCreatedBy(attachmentRequest.getCreatedBy());

            attachmentsRepository.save(attachment);

            log.info("File uploaded successfully.");
        } else {
            log.info("Invalid file type. Skipping file: {}", attachmentRequest.getFileName());
        }
    }

    private void processLinkAttachment(Session session, AttachmentRequest attachmentRequest) {
        log.info("Processing link: {}", attachmentRequest.getFileDownloadUri());

        // Create a new attachment entity for the link and associate it with the session
        Attachments attachment = new Attachments();
        attachment.setSession(session);
        attachment.setFileName(attachmentRequest.getFileName());
        attachment.setFileType("link");

        String attachmentNumber = generateAttachmentNumber("L");
        attachment.setAttachmentNumber(attachmentNumber);

        // Set other fields as needed.
        attachment.setCreatedAt(LocalDateTime.now());
        attachment.setCreatedBy("System");
        attachment.setLinkUrl(attachmentRequest.getFileDownloadUri());

        attachmentsRepository.save(attachment);

        log.info("Link uploaded successfully.");
    }

    private String generateAttachmentNumber(String attachmentType) {
        String randomAlphaNumeric = RandomStringUtils.randomAlphanumeric(8); // Generate 8-character random alphanumeric string
        return attachmentType + "-" + randomAlphaNumeric;
    }


}

