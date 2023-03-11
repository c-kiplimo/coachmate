package com.natujenge.thecouch.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Table(name = "tbl_sessionResources")
@Data
@Entity
public class SessionResources {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String links;
    private String uploads;

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
    Coach coach;

    @ManyToOne
    @JoinColumn(name = "client_id")
    Client client;

    @ManyToOne
    @JoinColumn(name = "org_id_id")
    Organization organization;
}
