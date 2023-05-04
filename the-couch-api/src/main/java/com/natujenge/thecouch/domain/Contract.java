package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.*;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_contracts")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contractNumber;
    private CoachingCategory coachingCategory;
    private String coachingTopic;
    private LocalDate startDate;
    private LocalDate endDate;


    private Float individualFeesPerSession;
    private Float groupFeesPerSession;
    private Integer noOfSessions;
    private Float amountDue;
    @Column(length = 5000, columnDefinition = "text")
    private String services;
    @Column(length = 5000, columnDefinition = "text")
    private String practice;
    @Column(length = 5000, columnDefinition = "text")
    private String terms_and_conditions;
    @Column(length = 5000, columnDefinition = "text")
    private String note;
    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Nullable
    @ManyToOne
    @JoinColumn(name="organization_id")
    Organization organization;

    // relations
    @ManyToOne
    @JoinColumn(name="client_id")
    User client;

    @ManyToOne
    @JoinColumn(name="coach_id")
    User coach;


}
