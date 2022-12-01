package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.CoachingPayment;
import com.natujenge.thecouch.domain.enums.CoachingVenue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "tbl_coaching_schedules")
public class CoachingSchedule {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    // includes date and time
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    private CoachingVenue coachingVenue;
    private String coachingDuration;
    @Enumerated(EnumType.STRING)
    private CoachingPayment coachingPayment;

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
