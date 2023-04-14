package com.natujenge.thecouch.service.dto;
import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.domain.NotificationSettings;
import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.domain.enums.PaymentType;
import com.natujenge.thecouch.domain.enums.SessionTemplateType;
import lombok.Data;


import java.time.LocalDateTime;
@Data
public class NotificationSettingsDTO {

    private Long id;

    private NotificationMode notificationMode;

    private String smsDisplayName;
    private String emailDisplayName;
    private boolean notificationEnable;

    private PaymentType paymentType;
    private String msisdn;
    private String tillNumber;
    private String accountNumber;
    private Float depositPercentage;
    private int paymentDue;

    CoachDTO coach;

    private LocalDateTime createdAt;

    private String createdBy;
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationMode getNotificationMode() {
        return notificationMode;
    }

    public void setBakerNotificationMode(NotificationMode notificationMode) {
        this.notificationMode = notificationMode;
    }

    public String getSmsDisplayName() {
        return smsDisplayName;
    }

    public void setSmsDisplayName(String smsDisplayName) {
        this.smsDisplayName = smsDisplayName;
    }

    public String getEmailDisplayName() {
        return emailDisplayName;
    }

    public void setEmailDisplayName(String emailDisplayName) {
        this.emailDisplayName = emailDisplayName;
    }

    public boolean isNotificationEnable() {
        return notificationEnable;
    }

    public void setNotificationEnable(boolean notificationEnable) {
        this.notificationEnable = notificationEnable;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getTillNumber() {
        return tillNumber;
    }

    public void setTillNumber(String tillNumber) {
        this.tillNumber = tillNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Float getDepositPercentage() {
        return depositPercentage;
    }

    public void setDepositPercentage(Float depositPercentage) {
        this.depositPercentage = depositPercentage;
    }

    public  CoachDTO getCoach() {
        return coach;
    }

    public void setCoach(CoachDTO coach) {
        this.coach = coach;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getPaymentDue() {
        return paymentDue;
    }

    public void setPaymentDue(int paymentDue) {
        this.paymentDue = paymentDue;
    }

    Organization organization;
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
    NotificationSettings notificationSettings;

    @Override
    public String toString() {
        return "BakerNotificationSettingsDTO{" +
                "id=" + id +
                ", bakerNotificationMode=" + notificationMode +
                ", smsDisplayName='" + smsDisplayName + '\'' +
                ", emailDisplayName='" + emailDisplayName + '\'' +
                ", notificationEnable=" + notificationEnable +
                ", paymentType=" + paymentType +
                ", msisdn='" + msisdn + '\'' +
                ", tillNumber='" + tillNumber + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", depositPercentage=" + depositPercentage +
                ", paymentDue=" + paymentDue +
                ", coach=" + coach +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }
}