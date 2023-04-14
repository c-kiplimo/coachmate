package com.natujenge.thecouch.service.dto;

import com.natujenge.thecouch.domain.enums.CoachStatus;
import java.time.LocalDateTime;


public class CoachDTO {

    private Long id;
    private String businessName;
    private String firstName;
    private String lastName;
    private String fullName;
    private String msisdn;
    private String emailAddress;
    private CoachStatus status;
    private boolean onboarded;
    private String reason;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;
    private NotificationSettingsDTO notificationSettings;

    private CoachPaymentDetailsDTO defaultCoachPaymentDetails;
    private CoachSettingsDTO coachSettings;
    private CoachSubscriptionDTO subscription;
    private CoachWalletDTO wallet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public CoachStatus   getStatus() {
        return status;
    }

        public void setStatus(CoachStatus status) {
        this.status = status;
    }

    public boolean isOnboarded() {
        return onboarded;
    }

    public void setOnboarded(boolean onboarded) {
        this.onboarded = onboarded;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public NotificationSettingsDTO getNotificationSettings() {
        return notificationSettings;
    }

    public void setNotificationSettings(NotificationSettingsDTO notificationSettings) {
        this.notificationSettings = notificationSettings;
    }


    public CoachPaymentDetailsDTO getDefaultPaymentDetails() {
        return defaultCoachPaymentDetails;
    }

    public void setDefaultBakerPaymentDetails(CoachPaymentDetailsDTO defaultBakerPaymentDetails) {
        this.defaultCoachPaymentDetails = defaultBakerPaymentDetails;
    }

    public CoachSettingsDTO getCoachSettings() {
        return coachSettings;
    }

    public void setBakerSettings(CoachSettingsDTO coachSettings) {
        this.coachSettings = coachSettings;
    }

    public CoachSubscriptionDTO getSubscription() {
        return subscription;
    }

    public void setSubscription(CoachSubscriptionDTO subscription) {
        this.subscription = subscription;
    }

    public CoachWalletDTO getWallet() {
        return wallet;
    }

    public void setWallet(CoachWalletDTO wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "BakerDTO{" +
                "id=" + id +
                ", businessName='" + businessName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", msisdn='" + msisdn + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", status=" + status +
                ", reason='" + reason + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", NotificationSettings=" + notificationSettings +
                ", defaultCoachPaymentDetails=" + defaultCoachPaymentDetails +
                ", coachSettings=" + coachSettings +
                ", subscription=" + subscription +
                ", wallet=" + wallet +
                '}';
    }
}
