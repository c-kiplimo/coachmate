package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.ClientStatus;
import com.natujenge.thecouch.domain.enums.ClientType;
import com.natujenge.thecouch.domain.enums.CoachStatus;
import com.natujenge.thecouch.domain.enums.PaymentMode;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table(name = "tbl_clients")
@Entity
public class Client {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private ClientType type;
    private  String msisdn;
    private String email_address;
    private  String physical_address;
    private  String profession;

    @Enumerated(EnumType.STRING)
    private PaymentMode payment_mode;

    @Enumerated(EnumType.STRING)
    private ClientStatus status;
    private String reason;

    //Management details

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;
    


}
