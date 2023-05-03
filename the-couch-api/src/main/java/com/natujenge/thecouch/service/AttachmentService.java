package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        log.info("Request to get attachment by session id: {} and coachId : {}", sessionId);

        List<Attachments> feedbackOptional = attachmentsRepository.findBySessionId(sessionId);

        return feedbackOptional;
    }

    public void uploadAttachments(Long sessionId, MultipartFile[] files, String[] links) throws Exception {
        // Get the session associated with the given sessionId
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new Exception("Session not found"));

        // Handle file uploads
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                // Check if the file is a valid type (PDF or DOCX)
                String fileType = file.getContentType();
                if (fileType.equals("application/pdf") || fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                    // Create a new attachment entity for each file and associate it with the session
                    Attachments attachment = new Attachments();
                    attachment.setSession(session);
                    attachment.setFileName(file.getOriginalFilename());
                    attachment.setFileType(file.getContentType());
                    attachment.setFileSize(file.getSize());
                    attachment.setFileContent(file.getBytes());
                    // Attachment Number Generation
                    int randNo = (int) ((Math.random() * (999 - 1)) + 1);
                    String attachmentL = String.format("%05d", randNo);
                    String attachmentNo = attachment.getSession().getName().charAt(0) + "-" + attachmentL;
                    attachment.setAttachmentNumber(attachmentNo);
                    attachmentsRepository.save(attachment);
                }
            }
        }

        // Handle link uploads
        if (links != null && links.length > 0) {
            for (String link : links) {
                // Check if the link is a valid video link (supports YouTube and Vimeo links)
                if (link.contains("youtube.com") || link.contains("vimeo.com")) {
                    // Create a new attachment entity for each link and associate it with the session
                    Attachments attachment = new Attachments();
                    attachment.setSession(session);
                    attachment.setFileName(link);
                    attachment.setFileType("link");
                    attachment.setLinkUrl(link);
                    // Attachment Number Generation
                    int randNo = (int) ((Math.random() * (99999 - 1)) + 1);
                    String attachmentL = String.format("%05d", randNo);
                    String attachmentNo = attachment.getSession().getName().charAt(0) + "-" + attachmentL;
                    attachment.setAttachmentNumber(attachmentNo);
                    attachmentsRepository.save(attachment);
                }
            }
        }
    }
}
