package com.natujenge.thecouch.domain;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tbl_account_statement")
public class AccountStatement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "date")
    private LocalDate date;

    @Column(name = "description")
    private String description;

    @Column(name = "balance_before")
    private float balanceBefore;

    @Column(name = "amount_in")
    private float amountIn;

    @Column(name = "balance_after")
    private float balanceAfter;
    @Column(name = "created_at")
    @UpdateTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;

    private String lastUpdatedBy;

    @OneToOne

    @JoinColumn(name = "client_id")
    User client;

    @OneToOne
    @JoinColumn(name = "coach_id")

    User coach;

    @OneToOne
    @JoinColumn(name = "organization_id")
    Organization organization;
}
