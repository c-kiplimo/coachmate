package com.natujenge.thecouch.service.dto;
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
    private OrganizationDTO organization;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public NotificationMode getNotificationMode() {
        return notificationMode;
    }

    public void setNotificationMode(NotificationMode notificationMode) {
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

    public CoachDTO getCoach() {
        return coach;
    }

    public void setCoach(CoachDTO coach) {
        this.coach = coach;
    }

    public OrganizationDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDTO organization) {
        this.organization = organization;
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



    public SessionTemplateType getSessionTemplateType() {
        return sessionTemplateType;
    }

    public void setSessionTemplateType(SessionTemplateType sessionTemplateType) {
        this.sessionTemplateType = sessionTemplateType;
    }

    public String getNewContractTemplate() {
        return newContractTemplate;
    }

    public void setNewContractTemplate(String newContractTemplate) {
        this.newContractTemplate = newContractTemplate;
    }

    public String getPartialBillPaymentTemplate() {
        return partialBillPaymentTemplate;
    }

    public void setPartialBillPaymentTemplate(String partialBillPaymentTemplate) {
        this.partialBillPaymentTemplate = partialBillPaymentTemplate;
    }

    public String getFullBillPaymentTemplate() {
        return fullBillPaymentTemplate;
    }

    public void setFullBillPaymentTemplate(String fullBillPaymentTemplate) {
        this.fullBillPaymentTemplate = fullBillPaymentTemplate;
    }

    public String getCancelSessionTemplate() {
        return cancelSessionTemplate;
    }

    public void setCancelSessionTemplate(String cancelSessionTemplate) {
        this.cancelSessionTemplate = cancelSessionTemplate;
    }

    public String getConductedSessionTemplate() {
        return conductedSessionTemplate;
    }

    public void setConductedSessionTemplate(String conductedSessionTemplate) {
        this.conductedSessionTemplate = conductedSessionTemplate;
    }

    public String getRescheduleSessionTemplate() {
        return rescheduleSessionTemplate;
    }

    public void setRescheduleSessionTemplate(String rescheduleSessionTemplate) {
        this.rescheduleSessionTemplate = rescheduleSessionTemplate;
    }

    public String getPaymentReminderTemplate() {
        return paymentReminderTemplate;
    }

    public void setPaymentReminderTemplate(String paymentReminderTemplate) {
        this.paymentReminderTemplate = paymentReminderTemplate;
    }

    public boolean isNewContractEnable() {
        return newContractEnable;
    }

    public void setNewContractEnable(boolean newContractEnable) {
        this.newContractEnable = newContractEnable;
    }

    public boolean isRescheduleSessionEnable() {
        return rescheduleSessionEnable;
    }

    public void setRescheduleSessionEnable(boolean rescheduleSessionEnable) {
        this.rescheduleSessionEnable = rescheduleSessionEnable;
    }

    public boolean isPartialBillPaymentEnable() {
        return partialBillPaymentEnable;
    }

    public void setPartialBillPaymentEnable(boolean partialBillPaymentEnable) {
        this.partialBillPaymentEnable = partialBillPaymentEnable;
    }

    public boolean isFullBillPaymentEnable() {
        return fullBillPaymentEnable;
    }

    public void setFullBillPaymentEnable(boolean fullBillPaymentEnable) {
        this.fullBillPaymentEnable = fullBillPaymentEnable;
    }

    public boolean isCancelSessionEnable() {
        return cancelSessionEnable;
    }

    public void setCancelSessionEnable(boolean cancelSessionEnable) {
        this.cancelSessionEnable = cancelSessionEnable;
    }

    public boolean isConductedSessionEnable() {
        return conductedSessionEnable;
    }

    public void setConductedSessionEnable(boolean conductedSessionEnable) {
        this.conductedSessionEnable = conductedSessionEnable;
    }

    public boolean isPaymentReminderEnable() {
        return paymentReminderEnable;
    }

    public void setPaymentReminderEnable(boolean paymentReminderEnable) {
        this.paymentReminderEnable = paymentReminderEnable;
    }
}