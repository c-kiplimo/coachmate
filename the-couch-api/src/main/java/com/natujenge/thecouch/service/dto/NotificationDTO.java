package ke.natujenge.baked.service.dto;

import ke.natujenge.baked.domain.enums.BakerNotificationMode;
import ke.natujenge.baked.domain.enums.NotificationSendStatus;
import ke.natujenge.baked.domain.enums.NotificationType;
import ke.natujenge.baked.domain.enums.OrderStatus;

import java.time.LocalDateTime;


public class NotificationDTO {

    private Long id;
    private NotificationType type;
    private String subject;
    private String srcAddress;
    private String dstAddress;
    private String content;
    private LocalDateTime sentAt;
    private BakerNotificationMode notificationMode;
    private NotificationSendStatus sendStatus;
    private String sendReason;
    private LocalDateTime deliveredAt;
    private String deliveryStatus;
    private Integer units;
    private Float cost;
    private String currency;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime lastUpdatedAt;

    private String lastUpdatedBy;
    private Long bakerId;
    private Long customerId;
    private Long orderId;
    private Long paymentId;
    private Long walletStatementId;

//    private Baker baker;
//    private Customer customer;
//    private Order order;
//    private Payment payment;
//
//    private WalletStatementDTO walletStatement;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSrcAddress() {
        return srcAddress;
    }

    public void setSrcAddress(String srcAddress) {
        this.srcAddress = srcAddress;
    }

    public String getDstAddress() {
        return dstAddress;
    }

    public void setDstAddress(String dstAddress) {
        this.dstAddress = dstAddress;
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

    public BakerNotificationMode getNotificationMode() {
        return notificationMode;
    }

    public void setNotificationMode(BakerNotificationMode notificationMode) {
        this.notificationMode = notificationMode;
    }

    public NotificationSendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(NotificationSendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getSendReason() {
        return sendReason;
    }

    public void setSendReason(String sendReason) {
        this.sendReason = sendReason;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public Long getBakerId() {
        return bakerId;
    }

    public void setBakerId(Long bakerId) {
        this.bakerId = bakerId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getWalletStatementId() {
        return walletStatementId;
    }

    public void setWalletStatementId(Long walletStatementId) {
        this.walletStatementId = walletStatementId;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", type='" + type + '\'' +
                ", srcAddress='" + srcAddress + '\'' +
                ", dstAddress='" + dstAddress + '\'' +
                ", content='" + content + '\'' +
                ", sentAt=" + sentAt +
                ", notificationMode=" + notificationMode +
                ", sendStatus=" + sendStatus +
                ", sendReason='" + sendReason + '\'' +
                ", deliveredAt=" + deliveredAt +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", units=" + units +
                ", cost=" + cost +
                ", currency='" + currency + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", bakerId=" + bakerId +
                ", customerId=" + customerId +
                ", orderId=" + orderId +
                ", paymentId=" + paymentId +
                ", walletStatementId=" + walletStatementId +
                '}';
    }
}
