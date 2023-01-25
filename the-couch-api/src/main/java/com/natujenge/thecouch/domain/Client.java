package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.ClientStatus;
import com.natujenge.thecouch.domain.enums.ClientType;
import com.natujenge.thecouch.domain.enums.PaymentMode;
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
@Table(name = "tbl_clients")
@Entity
public class Client {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    private Long id;
    private String fullName;
    private String firstName;
    private String lastName;

    @ManyToOne
    private Organization orgId;

    private String password;
    @Enumerated(EnumType.STRING)
    private ClientType clientType;
    private  String msisdn;

    @Column(name="email_address", unique = true)
    private String email;
    private  String physicalAddress;
    private  String profession;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @Enumerated(EnumType.STRING)
    private ClientStatus status;
    private String reason;


    //Management details
    @CreationTimestamp
    private LocalDateTime createdAt;
    private String createdBy;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;


    // Relations
    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;



    public void setName(String name) {
    }


}
