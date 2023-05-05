package com.natujenge.thecouch.service.dto;

import com.natujenge.thecouch.domain.enums.CoachingCategory;
import com.natujenge.thecouch.domain.enums.ContractStatus;
import com.natujenge.thecouch.domain.enums.OrgStatus;
import com.natujenge.thecouch.domain.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractDTO {


    private Long id;
    private String contractNumber;
    private CoachingCategory coachingCategory;
    private String coachingTopic;
    private LocalDate startDate;
    private LocalDate endDate;

    private Float individualFeesPerSession;
    private Float groupFeesPerSession;
    private Integer noOfSessions;
    private Float amountDue;
    private String objective;


    private String services;

    private String practice;
    private String terms_and_conditions;
    private String note;
    private ContractStatus contractStatus;
    private PaymentStatus paymentStatus;

    private Long organizationId;
    private String organizationName;
    private String organizationMsisdn;
    private String organizationEmail;
    private Long organizationSuperCoachId;
    private OrgStatus organizationStatus;


    // relations

    private Long clientId;
    private String clientFullName;
    private String clientFirstName;
    private String clientLastName;
    private String clientBusinessName;

    private Long coachId;
    private String coachFullName;
    private String coachFirstName;
    private String coachLastName;
    private String coachBusinessName;

}


