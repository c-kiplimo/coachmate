package com.natujenge.thecouch.web.rest.dto;
import lombok.*;
@Data

public class ClientDto {
    private String id;
    private String name;
    private String type;
    private String msisdn;
    private String email_address;
    private String physical_address;
    private String profession;
    private String payment_mode;
    private String status;
    private String reason;
    private String createdBy;
}
