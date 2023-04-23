package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.ClientStatus;
import com.natujenge.thecouch.domain.enums.ClientType;
import com.natujenge.thecouch.domain.enums.PaymentModeSubscription;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Data
@NoArgsConstructor
@Table(name = "tbl_clients")
@Entity
public class Client {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    private Long id;
    private String clientNumber;
    private String fullName;
    private String firstName;
    private String lastName;

    private String password;
    @Enumerated(EnumType.STRING)
    private ClientType clientType;
    @Column(unique = true)
    private String msisdn;
    @Column(unique = true)
    private String email;
    private  String physicalAddress;
    private  String profession;

    @Enumerated(EnumType.STRING)
    private PaymentModeSubscription paymentMode;

    @Enumerated(EnumType.STRING)
    private ClientStatus status;
    private String reason;
    @ManyToOne
    @JoinColumn(name="settings_id")
    NotificationSettings NotificationSettings;

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

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;



    public void setName(String name) {
    }


    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", clientType=" + clientType +
                ", msisdn='" + msisdn + '\'' +
                ", email='" + email + '\'' +
                ", physicalAddress='" + physicalAddress + '\'' +
                ", profession='" + profession + '\'' +
                ", paymentMode=" + paymentMode +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", coach=" + coach +
                ", organization=" + organization +
                '}';
    }

}
