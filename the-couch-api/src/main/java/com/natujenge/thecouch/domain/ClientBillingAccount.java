package com.natujenge.thecouch.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "tbl_client_billing_account")
public class ClientBillingAccount {
    @Id
    @GeneratedValue(strategy = GenerationType
            .IDENTITY)
    Long id;

    Float amountBilled;

    // Management Details
    @CreationTimestamp
    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false,name = "created_by")
    private String createdBy;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;

    private String lastUpdatedBy;

    @OneToOne
    @JoinColumn(name="client_id")
    User client;
    @OneToOne
    @JoinColumn(name="coach_id")
    User coach;
    @OneToOne
    @JoinColumn(name="organization_id")
    Organization organization;

}
