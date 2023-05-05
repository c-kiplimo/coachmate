package com.natujenge.thecouch.web.rest.dto;

import java.time.LocalDateTime;

public class CoachSubscriptionDTO {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long coachId;
    private Long planId;
    private String planName;
    private Long organizationId;
    private String organizationName;
    private CoachDTO coach;

}
