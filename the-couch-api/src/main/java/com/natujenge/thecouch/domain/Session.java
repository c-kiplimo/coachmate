package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.PaymentCurrency;
import com.natujenge.thecouch.domain.enums.SessionVenue;
import com.natujenge.thecouch.domain.enums.SessionStatus;
import com.natujenge.thecouch.domain.enums.SessionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_sessions")

public class Session {

    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String name;

    private Long orgId;
    private String sessionNumber;

    @Enumerated(EnumType.STRING)
    private SessionType sessionType;

    @Enumerated(EnumType.STRING)
    private SessionStatus sessionStatus;

    private String attachments;
    // session responses
    private String notes;
    private String feedback;

    // includes date and time
    private LocalDate sessionDate;
    private String sessionDuration;
    private String sessionStartTime;
    private String sessionEndTime;


    @Enumerated(EnumType.STRING)
    private SessionVenue sessionVenue;
    @Enumerated(EnumType.STRING)
    private PaymentCurrency paymentCurrency;
    private Float amountPaid;
    private Float sessionAmount;
    private Float sessionBalance;

    //Management details
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private String createdBy;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;

    // Relations
    @ManyToOne
    @JoinColumn(name = "session_schedules_id")
    SessionSchedules sessionSchedules;

    @ManyToOne
    @JoinColumn(name="client_id")
    User client;

    @ManyToOne
    @JoinColumn(name="contract_id")
    Contract contract;

    @ManyToOne
    @JoinColumn(name="coach_id")
    User coach;

}
