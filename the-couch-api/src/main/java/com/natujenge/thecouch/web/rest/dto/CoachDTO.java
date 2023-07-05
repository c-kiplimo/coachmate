package com.natujenge.thecouch.web.rest.dto;
import com.natujenge.thecouch.domain.enums.CoachStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CoachDTO {

    private Long id;
    private String businessName;
    private String firstName;
    private String lastName;
    private String fullName;
    private String msisdn;
    private String email;
    private CoachStatus coachStatus;
    private boolean onboarded;
    private String reason;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;

}

