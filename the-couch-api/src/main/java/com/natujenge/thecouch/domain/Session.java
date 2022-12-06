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

    @Enumerated(EnumType.STRING)
    private SessionVenue sessionVenue;
    @Enumerated(EnumType.STRING)
    private PaymentCurrency paymentCurrency;
    private String amountPaid;

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
    @JoinColumn(name="client_id")
    Client client;

    @ManyToOne
    @JoinColumn(name="contract_id")
    Contract contract;

    @ManyToOne
    @JoinColumn(name="coach_id")
    Coach coach;

}
