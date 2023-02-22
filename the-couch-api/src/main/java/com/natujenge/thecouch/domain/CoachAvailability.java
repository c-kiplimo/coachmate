package com.natujenge.thecouch.domain;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "coach_schedules")
@Data
public class CoachAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dayOfWeek;

    @ElementCollection
    @CollectionTable(name = "schedule_slots")
    private List<DaySchedule> slots;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coach coach;

}
