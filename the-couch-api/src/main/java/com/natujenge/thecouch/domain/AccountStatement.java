package com.natujenge.thecouch.domain;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tbl_account_statement")
public class accountStatement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "date")
    private LocalDate date;

    @Column(name = "description")
    private String description;

    @Column(name = "balance_before")
    private BigDecimal balanceBefore;

    @Column(name = "amount_in")
    private BigDecimal amountIn;

    @Column(name = "balance_after")
    private BigDecimal balanceAfter;
    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;

    private String lastUpdatedBy;

    @OneToOne
    @JoinColumn(name="client_id")
    Client client;

    @OneToOne
    @JoinColumn(name="coach_id")
    Coach coach;

    @OneToOne
    @JoinColumn(name="organization_id")
    Organization organization;
}
