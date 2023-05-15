package com.natujenge.thecouch.web.rest.dto;

import com.natujenge.thecouch.domain.enums.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClientDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String msisdn;
    private String email;
    private CoachStatus status;
    private UserRole userRole;

    private ClientType clientType;
    private ClientStatus clientStatus;
    private PaymentModeSubscription paymentMode;
    private  String profession;
    private  String physicalAddress;
    private String clientNumber;

    private String reason;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;
}
