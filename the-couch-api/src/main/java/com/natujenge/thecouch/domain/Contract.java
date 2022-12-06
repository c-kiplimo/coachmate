package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.CoachingCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private float feesPerPerson;
    private float individualFeesPerSession;
    private float groupFeesPerSession;
    private int noOfSessions;

    // relations
    @ManyToOne
    @JoinColumn(name="client_id")
    Client client;

    @ManyToOne
    @JoinColumn(name="coach_id")
    Coach coach;

}
