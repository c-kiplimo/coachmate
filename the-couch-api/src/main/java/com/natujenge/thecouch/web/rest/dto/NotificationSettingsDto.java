package com.natujenge.thecouch.web.rest.dto;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.domain.NotificationSettings;

public interface NotificationSettingsDto {
    Long getId();
    boolean isNotificationEnable();
    NotificationMode getNotificationMode();
    String getSmsDisplayName();
    String getEmailDisplayName();
    String getMsisdn();
    String getTillNumber();
    String getAccountNumber();
    Float getDepositPercentage();

    String getNewSessionTemplate();
    String getPartialBillPaymentTemplate();
    String getFullBillPaymentTemplate();
    String getCancelledSessionTemplate();
    String getConductedSessionTemplate();
    boolean isNewSessionEnable();
    boolean isPartialBillPaymentEnable();
    boolean isFullBillPaymentEnable();
    boolean isCancelSessionEnable();
    boolean isConductedSessionEnable();
}