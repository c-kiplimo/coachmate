package com.natujenge.thecouch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_sessionSchedules")
public class SessionSchedules {
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private Long orgId;

    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private boolean booked;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    User coach;
}
