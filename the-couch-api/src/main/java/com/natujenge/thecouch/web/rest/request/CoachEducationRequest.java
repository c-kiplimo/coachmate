package com.natujenge.thecouch.web.rest.request;

import lombok.Data;

import java.util.Date;

@Data
public class CoachEducationRequest {
    private Long id;
    private String courseName;
    private String provider;
    private Date dateIssued;
    private Date validTill;
    private String trainingHours;
    private String certificate;
    private String certificateUrl;
    private String createdBy;
    private Long coachId;
}
