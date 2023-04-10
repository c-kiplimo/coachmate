package com.natujenge.thecouch.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.domain.enums.PaymentMethod;
import lombok.*;

@Data
@Table(name = "tbl_coach_settings")
@Entity
public class CoachSettings {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String logo;
    @Enumerated(EnumType.STRING)
    @Column(name="business_notification_mode", length=10)
    private NotificationMode notificationMode;

    @Column(name="sms_display_name", length=50)
    private String smsDisplayName;

    @Column(name="email_display_name", length=50)
    private String emailDisplayName;

    @Column(name="notification_enable")
    private boolean notificationEnable;

    // payment settings
    @Enumerated(EnumType.STRING)
    @Column(name="payment_method",length = 10)
    private PaymentMethod paymentMethod;

    @Column(name="msisdn",length = 15)
    private String msisdn;

    @Column(name="till_number",length = 15)
    private String tillNumber;

    @Column(name="account_number",length = 25)
    private String accountNumber;

    @Column(name="deposit_percentage", columnDefinition = "float default 0")
    private float depositPercentage;


    // coach
    @ManyToOne
    @JoinColumn(name="coach_id")
    Coach coach;

    // organization
    @ManyToOne
    @JoinColumn(name="organization_id")
    Organization organization;
}
