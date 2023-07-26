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


    private LocalDate sessionDate; // if null, then it is a recurring session
    //all recurring sessions will be on the same day of the week
    //e.g. every Monday, every Tuesday, every Wednesday, every Thursday, every Friday, every Saturday, every Sunday
    //if sessionDate is not null, then it is a one-off session
    //if sessionDate is null, then this field must be set/if set to true, all sessions on this day of the week are available else they are not available
    private boolean recurring; //if set to true, then this session is recurring else it is a one-off session
    private LocalTime startTime;
    private LocalTime endTime;

    private boolean booked;

    @ManyToOne
    @JoinColumn(name = "dayOfTheWeek_id")
    private DaysOfTheWeek dayOfTheWeek;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    User coach;

    //created_at
    //updated_at
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
