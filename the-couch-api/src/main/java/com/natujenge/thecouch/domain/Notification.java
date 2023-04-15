package com.natujenge.thecouch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.domain.enums.SessionStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_notifications")
public class Notification {
    @Id
    @SequenceGenerator(
            name = "notification_sequence",
            sequenceName = "notification_sequence",
            allocationSize = 1
    )

    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "notification_sequence")
    private Long id;
    private String subject;
    private String sourceAddress;
    private String destinationAddress;
    private String content;
    private Long coachId;
    private Long clientId;
    private Long organizationId;

    private LocalDateTime sentAt;
    @Enumerated(EnumType.STRING)
    private NotificationMode notificationMode;

    @Enumerated(EnumType.STRING)
    private SessionStatus sendStatus;
    private String sendReason;

    private String paymentCurrency;

    // Management fields
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;

    private String lastUpdatedBy;


    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;
    @ManyToOne
    @JoinColumn(name = "contract_id")
    private  Contract contract;
    @ManyToOne
    @JoinColumn(name ="client_id")
    private Client client;
    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;






    /*
     * The notifications table will be used as part of service billing - more work to be done in the second sprint.
     * Notification service should be able to send back the delivery status back to the API service for updating
     * of the database object.
     */

}