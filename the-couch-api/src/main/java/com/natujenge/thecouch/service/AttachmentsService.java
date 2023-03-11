package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.repository.*;
import com.natujenge.thecouch.web.rest.dto.FeedbackDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void createNewAttachment(Long SessionId, Long coachId, Long orgIdId, Attachments attachments1) {

        Attachments attachments = new Attachments();

        //GEt Client
//        Optional<Client> client = clientRepository.findById(i);

        //GET SESSION
        Optional<Session> session = sessionRepository.findSessionById(SessionId);

        Optional<Client> client = clientRepository.findById(session.get().getClient().getId());

        Optional<Coach> coach = coachRepository.getCoachById(coachId);

        if(orgIdId != null) {
            Optional<Organization> organization = organizationRepository.findById(orgIdId);

            if(organization.isPresent()){
                attachments.setOrganization(organization.get());
            }
        }

        attachments.setClient(client.get());
        attachments.setSession(session.get());
        attachments.setCoach(coach.get());

        attachments.setUploads(attachments1.getUploads());
        attachments.setLinks(attachments1.getLinks());
        attachments.setCreatedBy(attachments1.getCreatedBy());
        attachments.setCreatedAt(attachments1.getCreatedAt());
        attachmentsRepository.save(attachments);



        log.info("attachment Saved!");
    }
    //get attachment by session id
    public List<Attachments> getAttachmentBySessionId(Long sessionId) {
        log.info("Request to get attachment by session id: {} and coachId : {}", sessionId);

        List<Attachments> feedbackOptional = attachmentsRepository.findBySessionId(sessionId);

        return feedbackOptional;
    }
}
