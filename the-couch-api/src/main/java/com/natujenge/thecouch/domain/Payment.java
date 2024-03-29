package com.natujenge.thecouch.domain;


import com.natujenge.thecouch.domain.enums.ModeOfPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_payments")
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String paymentRef;
    private String extPaymentRef;
    @Enumerated(EnumType.STRING)
    private ModeOfPayment modeOfPayment;
    private Float amount;
    private Float balanceAfter;
    private Float overPayment;
    private String narration;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;
    @ManyToOne
    @JoinColumn(name = "coach_id")
    private User coach;
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    private LocalDateTime paidAt;
    @CreationTimestamp
    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false,name = "created_by")
    private String createdBy;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;

    private String lastUpdatedBy;

}
