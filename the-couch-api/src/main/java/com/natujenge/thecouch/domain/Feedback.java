package com.natujenge.thecouch.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String  feedbackNumber;

    private Integer understandingScore;
    private Integer emotionalIntelligenceScore;
    private Integer listeningSkillsScore;
    private Integer clarificationScore;
    private Integer availabilityScore;
    private Integer overallScore;
    private String comments;

    // Management
    @CreationTimestamp
    private LocalDateTime createdAt;


    private String createdBy;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;

    @OneToOne
    @JoinColumn(name = "session_id")
    Session session;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    User coach;

    @ManyToOne
    @JoinColumn(name = "client_id")
    User client;

    @ManyToOne
    @JoinColumn(name = "org_id_id")
    Organization organization;



}
