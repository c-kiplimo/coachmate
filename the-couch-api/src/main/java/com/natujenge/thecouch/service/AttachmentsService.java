package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.repository.*;
import com.natujenge.thecouch.web.rest.dto.FeedbackDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AttachmentsService {
    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AttachmentsRepository attachmentsRepository;


    @Autowired
    CoachRepository coachRepository;

    @Autowired
    OrganizationRepository organizationRepository;
    //get attachment by session id
    public List<Attachments> getAttachmentBySessionId(Long sessionId) {
        log.info("Request to get attachment by session id: {} and coachId : {}", sessionId);

        List<Attachments> feedbackOptional = attachmentsRepository.findBySessionId(sessionId);

        return feedbackOptional;
    }
}
