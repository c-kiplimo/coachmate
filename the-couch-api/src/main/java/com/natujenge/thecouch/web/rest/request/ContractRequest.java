package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.enums.CoachingCategory;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ContractRequest {
    public CoachingCategory coachingCategory;
    public LocalDate startDate;
    public LocalDate endDate;
    public float feesPerPerson;
    public float individualFeesPerSession;
    public float groupFeesPerSession;
    public int noOfSessions;
    public long clientId;
    public  long coachId;
    public long organizationId;
    public String coachingTopic;
    private String services;
    private String practice;
    private String terms_and_conditions;
    private String note;


    public String objectives;
    public List<SessionRequest> sessions;
    // notification options
    public boolean sendNotification;

}