package com.natujenge.thecouch.service.dto;
import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.NotificationSendStatus;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.domain.enums.NotificationType;
import com.natujenge.thecouch.domain.enums.SessionStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


public class NotificationDTO {

    private Long id;
    private String subject;
    private String sourceAddress;
    private String destinationAddress;
    private String content;
    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    private NotificationMode notificationMode;

    @Enumerated(EnumType.STRING)
    private SessionStatus sendStatus;
    private String sendReason;

    private String paymentCurrency;

    // Management fields
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;

    private String lastUpdatedBy;


    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;
    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;
    @ManyToOne
    @JoinColumn(name ="client_id")
    private Client client;
    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public NotificationMode getNotificationMode() {
        return notificationMode;
    }

    public void setNotificationMode(NotificationMode notificationMode) {
        this.notificationMode = notificationMode;
    }

    public SessionStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(SessionStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getSendReason() {
        return sendReason;
    }

    public void setSendReason(String sendReason) {
        this.sendReason = sendReason;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
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

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", sourceAddress='" + sourceAddress + '\'' +
                ", destinationAddress='" + destinationAddress + '\'' +
                ", content='" + content + '\'' +
                ", sentAt=" + sentAt +
                ", notificationMode=" + notificationMode +
                ", sendStatus=" + sendStatus +
                ", sendReason='" + sendReason + '\'' +
                ", paymentCurrency='" + paymentCurrency + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", session=" + session +
                ", contract=" + contract +
                ", client=" + client +
                ", coach=" + coach +
                ", organization=" + organization +
                '}';
    }
}
