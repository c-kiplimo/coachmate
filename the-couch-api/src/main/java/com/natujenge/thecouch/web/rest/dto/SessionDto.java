package com.natujenge.thecouch.web.rest.dto;

import lombok.Data;
import org.hibernate.type.StringNVarcharType;
import org.springframework.boot.context.event.SpringApplicationEvent;

@Data
public class SessionDto {
    private Long id;
    private Integer session_no;
    private String name;
    private String type;
    private String details;
    private String notes;
    private String amount_paid;
    private String session_date;
    private String delivery_date;
    private String delivered_at;
    private String status;
    private String session_venue;
    private String attachments;
    private Long client_id;
    private Long coach_id;
    private String goals;
    private String feedback;
    private String createdBy;
}
