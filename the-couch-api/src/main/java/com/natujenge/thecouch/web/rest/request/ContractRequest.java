package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.enums.CoachingCategory;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public String coachingTopic;


    public List<String> objectives;
    public List<SessionRequest> sessions;


}
