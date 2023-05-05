package com.natujenge.thecouch.web.rest.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class SessionSchedulesDTO {
    private Long id;
    private Long orgId;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean booked;
    private Long coachId;
    private String coachFullName;
    private String coachMsisdn;
}
