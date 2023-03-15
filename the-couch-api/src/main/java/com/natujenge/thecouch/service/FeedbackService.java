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
public class FeedbackService {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    CoachRepository coachRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, OrganizationRepository organizationRepository
    , CoachRepository coachRepository, ClientRepository clientRepository) {
        this.feedbackRepository = feedbackRepository;
        this.coachRepository = coachRepository;
        this.clientRepository = clientRepository;
        this.organizationRepository = organizationRepository;
    }

    public void addNewFeedBack(Long SessionId, Long coachId, Long orgIdId, Feedback feedbackReq) {
        Feedback feedback = new Feedback();



        //GET SESSION
        Optional<Session> session = sessionRepository.findSessionById(SessionId);

        Optional<Client> client = clientRepository.findById(session.get().getClient().getId());

        Optional<Coach> coach = coachRepository.getCoachById(coachId);

        if(orgIdId != null) {
            Optional<Organization> organization = organizationRepository.findById(orgIdId);

            if(organization.isPresent()){
                feedback.setOrganization(organization.get());
            }
        }

        feedback.setClient(client.get());
        feedback.setSession(session.get());
        feedback.setCoach(coach.get());

        feedback.setAvailabilityScore(feedbackReq.getAvailabilityScore());
        feedback.setClarificationScore(feedbackReq.getClarificationScore());
        feedback.setEmotionalIntelligenceScore(feedbackReq.getEmotionalIntelligenceScore());
        feedback.setListeningSkillsScore(feedbackReq.getListeningSkillsScore());
        feedback.setComments(feedbackReq.getComments());
        feedback.setUnderstandingScore(feedbackReq.getUnderstandingScore());
        // compute overall score
        Integer totalScore = feedbackReq.getAvailabilityScore()+feedbackReq.getClarificationScore()+
                feedbackReq.getEmotionalIntelligenceScore()+ feedbackReq.getListeningSkillsScore()+
                feedbackReq.getUnderstandingScore();
        feedback.setOverallScore(totalScore);
        feedback.setCreatedBy(feedback.getCreatedBy());
        feedbackRepository.save(feedback);

        log.info("FeedBack Saved!");
    }

    public List<FeedbackDto> getFeedbackBySessionId(Long sessionId) {
        log.info("Request to get feedback by session id: {} and coachId : {}", sessionId);

        List<FeedbackDto> feedbackOptional = feedbackRepository.findBySessionId(sessionId);

        return feedbackOptional;
    }

    public FeedbackDto getFeedbackByClientId(Long clientId, Long coachId) {
        log.info("Request to get feedback by client id: {} and coachId : {}", clientId,coachId);

        Optional<FeedbackDto> feedbackOptional = feedbackRepository.findByClientIdAndCoachId(clientId,coachId);
        if (feedbackOptional.isPresent()) {
            return feedbackOptional.get();

        } else {
            throw new IllegalArgumentException("Feedback not found!");

        }
    }

    public List<FeedbackDto> getOrgFeedback(Long orgIdId) {
        Optional<Organization> optionalOrganization = organizationRepository.findById(orgIdId);
        return feedbackRepository.findByOrganization(optionalOrganization.get());
    }

    public List<FeedbackDto> getCoachFeedback(Long coachId) {
        Optional<Coach> optionalCoach = coachRepository.findCoachById(coachId);
        return feedbackRepository.findByCoach(optionalCoach.get());
    }
}
