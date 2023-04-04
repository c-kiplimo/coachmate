package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.ModeOfPayment;
import com.natujenge.thecouch.domain.enums.PaymentCurrency;
import com.natujenge.thecouch.domain.enums.StatementPeriod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "tbl_client_wallet")
public class ClientWallet {
    @Id
    @GeneratedValue(strategy = GenerationType
                    .IDENTITY)
    Long id;
    // updated on every payment
    Float walletBalanceBefore;
    Float walletBalance;
    Float amountDeposited;
    Float amountBilled;
    private String clientWalletNumber;
    private String extPaymentRef;
    public String description;

    @Enumerated(EnumType.STRING)
    private ModeOfPayment modeOfPayment;

    @Enumerated(EnumType.STRING)
    private PaymentCurrency paymentCurrency;


    // Management Details
    @CreationTimestamp
    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false,name = "created_by")
    private String createdBy;

    @UpdateTimestamp
    private LocalDate lastUpdatedAt;
    @Enumerated(EnumType.STRING)
    private StatementPeriod statementPeriod;

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
