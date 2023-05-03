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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_coaching_objectives")

public class CoachingObjective {

    /*
        * Goals
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String objective;


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
    User client;

    @ManyToOne
    @JoinColumn(name="contract_id")
    Contract contract;

    @ManyToOne
    @JoinColumn(name="coach_id")
    User coach;
    @ManyToOne
    @JoinColumn(name="organization_id")
    Organization organization;

}
