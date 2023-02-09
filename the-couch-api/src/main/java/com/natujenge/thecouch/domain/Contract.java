package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.CoachingCategory;
import com.natujenge.thecouch.domain.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_contracts")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private CoachingCategory coachingCategory;
    private String coachingTopic;
    private LocalDate startDate;
    private LocalDate endDate;


    private float individualFeesPerSession;
    private float groupFeesPerSession;
    private int noOfSessions;
    private float amountDue;


    // relations
    @ManyToOne
    @JoinColumn(name="client_id")
    Client client;

    @ManyToOne
    @JoinColumn(name="coach_id")
    Coach coach;

}
