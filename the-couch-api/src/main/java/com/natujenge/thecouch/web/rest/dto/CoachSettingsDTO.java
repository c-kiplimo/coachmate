package com.natujenge.thecouch.web.rest.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CoachSettingsDTO {

    private Long id;
    private String logo;
    private String logoUrl;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;
    private CoachDTO coach;


//    private NotificationSettingsDTO notificationSettings;
//
//    private CoachPaymentDetailsDTO defaultCoachPaymentDetails;

}
