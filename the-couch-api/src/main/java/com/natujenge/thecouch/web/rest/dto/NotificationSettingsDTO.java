package com.natujenge.thecouch.web.rest.dto;

import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.domain.enums.PaymentType;
import com.natujenge.thecouch.domain.enums.SessionTemplateType;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class NotificationSettingsDTO {

    private Long id;
    private String fileName;
    private NotificationMode notificationMode;
    private String smsDisplayName;
    private String emailDisplayName;
    private boolean notificationEnable;
    private PaymentType paymentType;
    private String msisdn;
    private String tillNumber;
    private String accountNumber;
    private Float depositPercentage;

    // coach
    private CoachDTO coach;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;
    private Organization organization;

    // Session Settings
    private SessionTemplateType sessionTemplateType;
    private String newContractTemplate;
    private String partialBillPaymentTemplate;
    private String fullBillPaymentTemplate;
    private String cancelSessionTemplate;
    private String conductedSessionTemplate;
    private String rescheduleSessionTemplate;
    private String paymentReminderTemplate;
    private boolean newContractEnable;
    private boolean rescheduleSessionEnable;
    private boolean partialBillPaymentEnable;
    private boolean fullBillPaymentEnable;
    private boolean cancelSessionEnable;
    private boolean conductedSessionEnable;
    private boolean paymentReminderEnable;
}