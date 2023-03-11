package com.natujenge.thecouch.web.rest.dto;

import com.natujenge.thecouch.domain.enums.NotificationMode;

import java.time.LocalDateTime;

public interface NotificationDto {

    public Long getId();
    public NotificationMode getNotificationMode();
    public String getSubject();
    public String getSrcAddress();
    public String getDstAddress();
    public String getContent();
    public LocalDateTime getSentAt();
    public String getSendReason();
    public String getDeliveryStatus();
    public int getUnits() ;
    public float getCost();
    public CoachView getCoach();
    public ClientView getClient();
    public PaymentView getPayment();

    // baker details to send client
    public interface  CoachView {
        public Long getId();
        public String getFirstName();
    }
    // customer dto
    public interface  ClientView {
        public Long getId();
        String getFirstName();
        String getLastName();

    }
    // order dto
    public interface  sessionView {
        public Long getId();
        public String getName();
    }

    // payment dto
    public interface PaymentView {
        public Long getId();
        public Float getAmount();
    }
}
