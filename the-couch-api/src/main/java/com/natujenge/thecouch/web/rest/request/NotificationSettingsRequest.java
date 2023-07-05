package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.NotificationSettings;
import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.domain.enums.SessionTemplateType;
import lombok.Data;

@Data
public class NotificationSettingsRequest {
    // General Settings
    private NotificationMode notificationMode;
    private String smsDisplayName;
    private String emailDisplayName;
    private boolean notificationEnable;


    // payment settings
    private String msisdn;
    private String tillNumber;
    private String accountNumber;
    private Float depositPercentage;

    private User coach;
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
    private String createdBy;

    private boolean newContractEnable;
    private boolean rescheduleSessionEnable;
    private boolean partialBillPaymentEnable;
    private boolean fullBillPaymentEnable;
    private boolean cancelSessionEnable;
    private boolean conductedSessionEnable;
    private boolean paymentReminderEnable;
    NotificationSettings notificationSettings;

    // Include General Enable Field for all notifications
}