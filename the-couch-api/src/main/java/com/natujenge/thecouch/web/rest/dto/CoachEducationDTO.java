package com.natujenge.thecouch.web.rest.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CoachEducationDTO {
    private Long id;
    private String courseName;
    private String provider;
    private Date dateIssued;
    private Date validTill;
    private String trainingHours;
    private String certificateUrl;
    private String createdBy;
    private UserDTO coach;
    private Date createdAt;
    private Date lastUpdatedAt;
    private String lastUpdatedBy;
}
