package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.enums.CoachingCategory;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContractRequest {
    public CoachingCategory coachingCategory;
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public float feesPerPerson;
    public float individualFeesPerSession;
    public float groupFeesPerSession;
    public int noOfSessions;

    public List<String> objectives;
    public List<ScheduleRequest> schedules;


}
