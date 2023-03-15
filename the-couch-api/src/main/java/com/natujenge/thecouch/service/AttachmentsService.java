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
    public void createNewAttachment(MultipartFile file, String link, Long sessionId, Long coachId, Long orgIdId) throws IOException {

        Attachments attachments = new Attachments();

        // GET SESSION
        Optional<Session> session = sessionRepository.findSessionById(sessionId);

        Optional<Client> client = clientRepository.findById(session.get().getClient().getId());

        Optional<Coach> coach = coachRepository.getCoachById(coachId);

        if (orgIdId != null) {
            Optional<Organization> organization = organizationRepository.findById(orgIdId);

            if (organization.isPresent()) {
                attachments.setOrganization(organization.get());
            }
        }

        attachments.setClient(client.get());
        attachments.setSession(session.get());
        attachments.setCoach(coach.get());
        attachments.setLink(link);
        attachments.setFile(file.getBytes());
        attachments.setCreatedBy(coach.get().getFirstName() + " " + coach.get().getLastName());
        attachmentsRepository.save(attachments);

        log.info("Attachment saved successfully!");
    }

    //get attachment by session id
    public List<Attachments> getAttachmentBySessionId(Long sessionId) {
        log.info("Request to get attachment by session id: {} and coachId : {}", sessionId);

        List<Attachments> feedbackOptional = attachmentsRepository.findBySessionId(sessionId);

        return feedbackOptional;
    }
}
