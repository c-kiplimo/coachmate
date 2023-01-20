package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.Feedback;
import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.repository.ClientRepository;
import com.natujenge.thecouch.repository.FeedbackRepository;
import com.natujenge.thecouch.repository.SessionRepository;
import com.natujenge.thecouch.web.rest.dto.FeedbackDto;
import com.natujenge.thecouch.web.rest.dto.SessionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void addNewFeedBack(Long id,Long coachId, Feedback feedback) {
        // Get associated session
        Optional<Session> optionalSession = sessionRepository.getSessionByIdAndCoachId(id,coachId);
        if (optionalSession.isEmpty()){
            throw new IllegalArgumentException("Session Not Found!");
        }

        // Get associated Client
        Optional<Client> optionalClient = clientRepository.findClientByIdAndCoachId(id,coachId);
        if (optionalClient.isEmpty()){
            throw new IllegalArgumentException("Client Not Found!");
        }
        feedback.setClient(optionalClient.get());
        feedback.setSession(optionalSession.get());

        // compute overall score
        Integer totalScore = feedback.getAvailabilityScore()+feedback.getClarificationScore()+
                feedback.getEmotionalIntelligenceScore()+ feedback.getListeningSkillsScore()+
                feedback.getUnderstandingScore();
        feedback.setOverallScore(totalScore);

        feedback.setCreatedBy(optionalClient.get().getFullName());
        feedback.setLastUpdatedBy(optionalClient.get().getFullName());
        feedbackRepository.save(feedback);

        log.info("FeedBack Saved!");
    }

    public FeedbackDto getFeedbackBySessionId(Long sessionId, Long coachId) {
        log.info("Request to get feedback by session id: {} and coachId : {}", sessionId,coachId);

        Optional<FeedbackDto> feedbackOptional = feedbackRepository.findBySessionIdAndCoachId(sessionId,coachId);
        if (feedbackOptional.isPresent()) {
            return feedbackOptional.get();

        } else {
            throw new IllegalArgumentException("Feedback not found!");

        }
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
}
