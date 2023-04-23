package com.natujenge.thecouch.service.dto;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.domain.OrganizationSubscription;
import com.natujenge.thecouch.domain.enums.OrgStatus;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    private String secondName;
    private String fullName;
    private OrganizationSubscription subscription;
    private PaymentDetailsDTO paymentDetails;
    private NotificationDTO notification;
    private NotificationSettingsDTO notificationSettings;
;

    private Long superCoachId; //user id

    @Enumerated(EnumType.STRING)
    private OrgStatus status;

    //Management details

    private LocalDateTime createdAt;
    private String createdBy;

    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;

    //Relations
    private List<Client> clients;

    private List<Coach> coaches;

}
