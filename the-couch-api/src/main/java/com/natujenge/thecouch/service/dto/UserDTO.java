package com.natujenge.thecouch.service.dto;

import com.natujenge.thecouch.domain.ContractTemplate;
import com.natujenge.thecouch.domain.NotificationSettings;
import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String fullName;
    private String firstName;
    private String lastName;
    private String businessName;

    User addedBy; //user - coach id

    private String msisdn;

    private String email;

    // LOGIN DETAILS
    private String username; //email
    private String password;
    private UserRole userRole;


    //CLIENT DETAILS

    private ClientType clientType;


    private ClientStatus clientStatus;


    private PaymentModeSubscription paymentMode;
    private  String profession;
    private  String physicalAddress;
    private String clientNumber;


    //COACH DETAILS

    private CoachStatus coachStatus;
    private boolean onboarded; //COACH ADDED BY ORGANIZATION
    private String coachNumber;

    //FOR ONBOARDED COACH AND ALL CLIENTS
    private String reason;


    private String createdBy; //full name
    private String lastUpdatedBy; //full name


    // management fields

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;

    private ContentStatus contentStatus;

    // Object Relationships

    Organization organization;


//    NotificationSettings notificationSettings;

//    ContractTemplate contractTemplate;

    // Access fields
    private Boolean locked = false;
    private Boolean enabled = false;

}
