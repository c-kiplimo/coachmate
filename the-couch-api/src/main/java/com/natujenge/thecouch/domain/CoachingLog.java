package com.natujenge.thecouch.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Table(name = "tbl_coaching_log")
@Data
@Entity
public class CoachingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String  noInGroup;
    private String clientName;
    private String  clientEmail;
    private Date startDate;
    private Date endDate;

    private Long paidHours;
    private Long proBonoHours;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private String createdBy;

    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    User coach;

}
