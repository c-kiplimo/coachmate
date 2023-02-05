package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.ModeOfPayment;
import com.natujenge.thecouch.domain.enums.PaymentCurrency;
import lombok.AllArgsConstructor;
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
@Table(name = "tbl_client_wallet")
public class ClientWallet {
    @Id
    @GeneratedValue(strategy = GenerationType
                    .IDENTITY)
    Long id;
    // updated on every payment
    Float walletBalance;
    Float amountDeposited;
    private String paymentRef;
    private String extPaymentRef;
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
    private LocalDateTime lastUpdatedAt;

    private String lastUpdatedBy;

    @OneToOne
    @JoinColumn(name="client_id")
    Client client;

}
