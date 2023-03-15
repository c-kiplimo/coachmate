package com.natujenge.thecouch.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Table(name = "tbl_coach_settings")
@Entity

public class CoachSettings {
    @SequenceGenerator(
            name = "coach_settings_sequence",
            sequenceName = "coach_settings_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "coach_settings_sequence")
    @Id
    private Long id;
    private String logo; // TODO: Discuss Appropriate Object to use
    // table >> notification_settings TODO: Review Table relationships
    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "coach_id"
    )
    private Coach coach;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private String createdBy;
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;
}
