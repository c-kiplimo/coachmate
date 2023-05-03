package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.repository.*;
import com.natujenge.thecouch.web.rest.dto.FeedbackDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FeedbackService {


   private final SessionRepository sessionRepository;
  private final  FeedbackRepository feedbackRepository;


    private final UserRepository userRepository;


    private final OrganizationRepository organizationRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, OrganizationRepository organizationRepository
    , SessionRepository sessionRepository, UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }

    public void addNewFeedBack(Long sessionId, Long coachId, Long orgId, Feedback feedbackReq) {
        Feedback feedback = new Feedback();

        // Get session
        Optional<Session> session = sessionRepository.findSessionById(sessionId);
        Optional<User> client = userRepository.findById(session.get().getClient().getId());

        // Set coach or organization based on ID present
        if (coachId != null) {
            Optional<User> coach = userRepository.findById(coachId);
            feedback.setCoach(coach.get());
        } else if (orgId != null) {
            Optional<Organization> organization = organizationRepository.findById(orgId);
            feedback.setOrganization(organization.get());
        }

        feedback.setClient(client.get());
        feedback.setSession(session.get());

        // Generate feedback number
        int randNo = (int) ((Math.random() * (999 - 1)) + 1);
        String feedbackL = String.format("%05d", randNo);
        String feedbackNo = feedback.getCoach().getBusinessName().substring(0, 2) +
                feedback.getClient().getFirstName().charAt(0) + feedback.getSession().getName().charAt(0) + "-" + feedbackL;
        feedback.setFeedbackNumber(feedbackNo);

        // Set feedback scores and compute overall score
        feedback.setAvailabilityScore(feedbackReq.getAvailabilityScore());
        feedback.setClarificationScore(feedbackReq.getClarificationScore());
        feedback.setEmotionalIntelligenceScore(feedbackReq.getEmotionalIntelligenceScore());
        feedback.setListeningSkillsScore(feedbackReq.getListeningSkillsScore());
        feedback.setComments(feedbackReq.getComments());
        feedback.setUnderstandingScore(feedbackReq.getUnderstandingScore());
        Integer totalScore = feedbackReq.getAvailabilityScore() + feedbackReq.getClarificationScore() +
                feedbackReq.getEmotionalIntelligenceScore() + feedbackReq.getListeningSkillsScore() +
                feedbackReq.getUnderstandingScore();
        feedback.setOverallScore(totalScore);

        // Save feedback to database
        feedback.setCreatedBy(feedback.getCreatedBy());
        feedbackRepository.save(feedback);

        log.info("Feedback Saved!");
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
        Optional<User> optionalCoach = userRepository.findById(coachId);
        return feedbackRepository.findByCoach(optionalCoach.get());
    }
}
