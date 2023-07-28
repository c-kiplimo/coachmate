package com.natujenge.thecouch.web.rest.dto;

import com.natujenge.thecouch.domain.enums.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SessionDTO {
    private Long id;
    private String name;
    private String sessionNumber;
    private SessionType sessionType;
    private SessionStatus sessionStatus;
    private String notes;
    private String feedback;
    private SessionVenue sessionVenue;
    private PaymentCurrency paymentCurrency;
    private String amountPaid;
    private Long sessionSchedulesId;
    private Long sessionSchedulesOrgId;
    private LocalDate sessionSchedulesSessionDate;
    private LocalDate sessionDate;
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
