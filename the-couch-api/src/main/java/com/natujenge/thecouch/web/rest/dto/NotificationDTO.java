package com.natujenge.thecouch.web.rest.dto;

import com.natujenge.thecouch.domain.Contract;
import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.domain.enums.SessionStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private String subject;
    private String sourceAddress;
    private String destinationAddress;
    private String content;
    private Long coachId;
    private Long clientId;
    private Long organizationId;

    private LocalDateTime sentAt;
    private NotificationMode notificationMode;

    private SessionStatus sendStatus;
    private String sendReason;
    private String paymentCurrency;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;

    private Session session;
    private Contract contract;
}
