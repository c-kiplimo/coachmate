package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.CoachingSchedule;
import com.natujenge.thecouch.domain.enums.CoachingPayment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleRequest {
    public LocalDateTime date;
    public CoachingSchedule coachingSchedule;
    public String coachingDuration;
    public CoachingPayment coachingPayment;
}
