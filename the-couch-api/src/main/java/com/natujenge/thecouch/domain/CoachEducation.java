package com.natujenge.thecouch.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "tbl_coach_education")
@Entity
public class CoachEducation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    private Long id;
    private String courseName;
    private String provider;
    private Date dateIssued;
    private Date validTill;
    private String trainingHours;
    private String certificateUrl;
    private String createdBy;

    //Relations
    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;



}
