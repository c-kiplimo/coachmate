package com.natujenge.thecouch.web.rest.dto;

public interface FeedbackDto {
    Integer getUnderstandingScore();

    Integer getEmotionalIntelligenceScore();

    Integer getListeningSkillsScore();

    Integer getClarificationScore();

    Integer getAvailabilityScore();

    Integer getOverallScore();

    String getComments();

    SessionView getSession();

    ClientView getClient();

    // session details
    public interface SessionView {
        Long getId();
        String getName();
    }

    // client details
    public interface ClientView {
        Long getId();
        String getFirstName();
        String getMsisdn();
    }


}
