package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.CoachStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Table(name = "tbl_coaches")
@Entity
public class Coach implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    private Long id;

    @ManyToOne
    private Organization orgId;

    private String businessName;
    private String fullName;
    private String firstName;
    private String lastName;
    private String msisdn;
    private String emailAddress;
    @Enumerated(EnumType.STRING)
    private CoachStatus status;
    private String reason;

    // management Details
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private String createdBy;
    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;

}
