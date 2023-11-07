package com.natujenge.thecouch.web.rest.dto;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.OrgStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class OrganizationDTO {
    private Long id;
    private String orgName;
    private String msisdn;
    private String email;
    private String address; //remove
    private String firstName;
    private String lastName;
    private String fullName;
    private OrganizationSubscription subscription;
    private PaymentDetails paymentDetails;
    private Notification notification;
    private NotificationSettings notificationSettings;
;

    private Long superCoachId; //user id

    private OrgStatus status;

    //Management details

    private LocalDateTime createdAt;
    private String createdBy;

    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;

    //Relations
    private List<User> clients;

    private List<User> coaches;

    @Override
    public String toString() {
        return "OrganizationDTO{" +
                "id=" + id +
                ", orgName='" + orgName + '\'' +
                ", msisdn='" + msisdn + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", subscription=" + subscription +
                ", paymentDetails=" + paymentDetails +
                ", notification=" + notification +
                ", notificationSettings=" + notificationSettings +
                ", superCoachId=" + superCoachId +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", clients=" + clients +
                ", coaches=" + coaches +
                '}';
    }
}
