package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.User;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class CoachingLogRequest {
    private Long id;
    private String  noInGroup;
    private String clientName;
    private String  clientEmail;
    private Date startDate;
    private Date endDate;

    private Long paidHours;
    private Long proBonoHours;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;
    User coach;
}
