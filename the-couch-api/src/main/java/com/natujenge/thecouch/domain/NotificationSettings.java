package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.ModeOfPayment;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.domain.enums.SessionTemplateType;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_notification_settings")
@Entity
public class NotificationSettings {
    @SequenceGenerator(
            name = "notification_settings_sequence",
            sequenceName = "notification_settings_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_settings_sequence")
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationMode notificationMode;

    private String smsDisplayName;
    private String emailDisplayName;
    private boolean notificationEnable;

    //payment settings
    @Enumerated(EnumType.STRING)
    private ModeOfPayment modeOfPayment;
    private String msisdn;
    private String tillNumber;
    private String accountNumber;




    // session settings
    @Enumerated(EnumType.STRING)
    private SessionTemplateType sessionTemplateType;
    // Both email and sms have the similar Templates
    private String newSessionTemplate;
    private String newContractTemplate;
    private String partialBillPaymentTemplate;
    private String fullBillPaymentTemplate;
    private String cancelSessionTemplate;
    private String conductedSessionTemplate;
    private String rescheduleSessionTemplate;
    private String paymentReminderTemplate;

    // Enable template Fields
    private boolean newSessionEnable;
    private boolean newContractEnable;
    private boolean partialBillPaymentEnable;
    private boolean fullBillPaymentEnable;
    private boolean cancelSessionEnable;
    private boolean conductedSessionEnable;
    private boolean rescheduleSessionEnable;

    @ManyToOne
    @JoinColumn(name="coach_id")
    Coach coach;
    @ManyToOne
    @JoinColumn(name="organization_id")
    Organization organization;

    // Management fields
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;

}