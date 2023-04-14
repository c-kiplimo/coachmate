package ke.natujenge.baked.service.dto;

import ke.natujenge.baked.domain.BakerNotificationSettings;
import ke.natujenge.baked.domain.enums.BakerStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


public class BakerDTO {

    private Long id;
    private String businessName;
    private String firstName;
    private String lastName;
    private String fullName;
    private String msisdn;
    private String emailAddress;
    private BakerStatus status;
    private boolean onboarded;
    private String reason;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;
    private BakerNotificationSettingsDTO bakerNotificationSettings;
    private BakerLocationDTO defaultBakerLocation;
    private BakerPaymentDetailsDTO defaultBakerPaymentDetails;
    private BakerSettingsDTO bakerSettings;
    private BakerSubscriptionDTO subscription;
    private BakerWalletDTO wallet;

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

    public BakerStatus getStatus() {
        return status;
    }

    public void setStatus(BakerStatus status) {
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

    public BakerNotificationSettingsDTO getBakerNotificationSettings() {
        return bakerNotificationSettings;
    }

    public void setBakerNotificationSettings(BakerNotificationSettingsDTO bakerNotificationSettings) {
        this.bakerNotificationSettings = bakerNotificationSettings;
    }

    public BakerLocationDTO getDefaultBakerLocation() {
        return defaultBakerLocation;
    }

    public void setDefaultBakerLocation(BakerLocationDTO defaultBakerLocation) {
        this.defaultBakerLocation = defaultBakerLocation;
    }

    public BakerPaymentDetailsDTO getDefaultBakerPaymentDetails() {
        return defaultBakerPaymentDetails;
    }

    public void setDefaultBakerPaymentDetails(BakerPaymentDetailsDTO defaultBakerPaymentDetails) {
        this.defaultBakerPaymentDetails = defaultBakerPaymentDetails;
    }

    public BakerSettingsDTO getBakerSettings() {
        return bakerSettings;
    }

    public void setBakerSettings(BakerSettingsDTO bakerSettings) {
        this.bakerSettings = bakerSettings;
    }

    public BakerSubscriptionDTO getSubscription() {
        return subscription;
    }

    public void setSubscription(BakerSubscriptionDTO subscription) {
        this.subscription = subscription;
    }

    public BakerWalletDTO getWallet() {
        return wallet;
    }

    public void setWallet(BakerWalletDTO wallet) {
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
                ", bakerNotificationSettings=" + bakerNotificationSettings +
                ", defaultBakerLocation=" + defaultBakerLocation +
                ", defaultBakerPaymentDetails=" + defaultBakerPaymentDetails +
                ", bakerSettings=" + bakerSettings +
                ", subscription=" + subscription +
                ", wallet=" + wallet +
                '}';
    }
}
