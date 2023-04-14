package com.natujenge.thecouch.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestData {
    private String businessName;
    private String bakerMsisdn;
    private String bakerEmail;
    private Long bakerId;
    private String message;
    private String subject;
    private Long orderId;
    private Long paymentId;
    private Long customerId;
}
