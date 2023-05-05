package com.natujenge.thecouch.web.rest.dto;

import com.natujenge.thecouch.domain.enums.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SessionDTO {
    private Long id;
    private String Name;
    private String SessionDate;
    private String SessionStartTime;
    private String SessionEndTime;
    private String SessionNumber;
    private SessionType SessionType;
    private SessionStatus SessionStatus;
    private String Notes;
    private String Feedback;
    private SessionVenue SessionVenue;
    private PaymentCurrency PaymentCurrency;
    private String AmountPaid;
    private Long sessionSchedulesId;
    private Long sessionSchedulesOrgId;
    private LocalDate sessionSchedulesSessionDate;
    private LocalTime sessionSchedulesStartTime;
    private LocalTime sessionSchedulesEndTime;
    private boolean sessionSchedulesBooked;
    private Long clientId;
    private String clientFullName;
    private ClientType clientType;
    private String clientMsisdn;
    private Long coachId;
    private Long contractId;
    private CoachingCategory contractCoachingCategory;
    private String contractCoachingTopic;
}
