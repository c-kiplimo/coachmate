package com.natujenge.thecouch.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestData {
    private String businessName;
    private String coachMsisdn;
    private String coachEmail;
    private Long coachId;
    private String message;
    private String subject;
    private String organizationName;
    private Long organizationId;
    private String clientMsisdn;
    private String clientEmail;
    private Long clientId;
}
