package com.natujenge.thecouch.web.rest.request;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import lombok.Data;
import com.natujenge.thecouch.domain.enums.SessionTemplateType;

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

    // Include General Enable Field for all notifications
}