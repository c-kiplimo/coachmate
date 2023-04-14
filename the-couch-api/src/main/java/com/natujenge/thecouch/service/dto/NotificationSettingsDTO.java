package ke.natujenge.baked.service.dto;

import ke.natujenge.baked.domain.enums.BakerNotificationMode;
import ke.natujenge.baked.domain.enums.OrderTemplateType;
import ke.natujenge.baked.domain.enums.PaymentType;

import java.time.LocalDateTime;

public class BakerNotificationSettingsDTO {

    private Long id;

    private BakerNotificationMode bakerNotificationMode;

    private String smsDisplayName;
    private String emailDisplayName;
    private boolean notificationEnable;

    private PaymentType paymentType;
    private String msisdn;
    private String tillNumber;
    private String accountNumber;
    private Float depositPercentage;
    private OrderTemplateType orderTemplateType;
    private String newOrderTemplate;
    private String partialOrderPaymentTemplate;
    private String fullOrderPaymentTemplate;
    private String cancelOrderTemplate;
    private String deliverOrderTemplate;
    private String paymentReminderTemplate;

    private boolean newOrderEnable;
    private boolean partialOrderPaymentEnable;
    private boolean fullOrderPaymentEnable;
    private boolean cancelOrderEnable;
    private boolean deliverOrderEnable;
    private int paymentDue;

    BakerDTO baker;

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

    public BakerNotificationMode getBakerNotificationMode() {
        return bakerNotificationMode;
    }

    public void setBakerNotificationMode(BakerNotificationMode bakerNotificationMode) {
        this.bakerNotificationMode = bakerNotificationMode;
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

    public OrderTemplateType getOrderTemplateType() {
        return orderTemplateType;
    }

    public void setOrderTemplateType(OrderTemplateType orderTemplateType) {
        this.orderTemplateType = orderTemplateType;
    }

    public String getNewOrderTemplate() {
        return newOrderTemplate;
    }

    public void setNewOrderTemplate(String newOrderTemplate) {
        this.newOrderTemplate = newOrderTemplate;
    }

    public String getPartialOrderPaymentTemplate() {
        return partialOrderPaymentTemplate;
    }

    public void setPartialOrderPaymentTemplate(String partialOrderPaymentTemplate) {
        this.partialOrderPaymentTemplate = partialOrderPaymentTemplate;
    }

    public String getFullOrderPaymentTemplate() {
        return fullOrderPaymentTemplate;
    }

    public void setFullOrderPaymentTemplate(String fullOrderPaymentTemplate) {
        this.fullOrderPaymentTemplate = fullOrderPaymentTemplate;
    }

    public String getCancelOrderTemplate() {
        return cancelOrderTemplate;
    }

    public void setCancelOrderTemplate(String cancelOrderTemplate) {
        this.cancelOrderTemplate = cancelOrderTemplate;
    }

    public String getDeliverOrderTemplate() {
        return deliverOrderTemplate;
    }

    public void setDeliverOrderTemplate(String deliverOrderTemplate) {
        this.deliverOrderTemplate = deliverOrderTemplate;
    }

    public String getPaymentReminderTemplate() {
        return paymentReminderTemplate;
    }

    public void setPaymentReminderTemplate(String paymentReminderTemplate) {
        this.paymentReminderTemplate = paymentReminderTemplate;
    }

    public boolean isNewOrderEnable() {
        return newOrderEnable;
    }

    public void setNewOrderEnable(boolean newOrderEnable) {
        this.newOrderEnable = newOrderEnable;
    }

    public boolean isPartialOrderPaymentEnable() {
        return partialOrderPaymentEnable;
    }

    public void setPartialOrderPaymentEnable(boolean partialOrderPaymentEnable) {
        this.partialOrderPaymentEnable = partialOrderPaymentEnable;
    }

    public boolean isFullOrderPaymentEnable() {
        return fullOrderPaymentEnable;
    }

    public void setFullOrderPaymentEnable(boolean fullOrderPaymentEnable) {
        this.fullOrderPaymentEnable = fullOrderPaymentEnable;
    }

    public boolean isCancelOrderEnable() {
        return cancelOrderEnable;
    }

    public void setCancelOrderEnable(boolean cancelOrderEnable) {
        this.cancelOrderEnable = cancelOrderEnable;
    }

    public boolean isDeliverOrderEnable() {
        return deliverOrderEnable;
    }

    public void setDeliverOrderEnable(boolean deliverOrderEnable) {
        this.deliverOrderEnable = deliverOrderEnable;
    }

    public BakerDTO getBaker() {
        return baker;
    }

    public void setBaker(BakerDTO baker) {
        this.baker = baker;
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

    @Override
    public String toString() {
        return "BakerNotificationSettingsDTO{" +
                "id=" + id +
                ", bakerNotificationMode=" + bakerNotificationMode +
                ", smsDisplayName='" + smsDisplayName + '\'' +
                ", emailDisplayName='" + emailDisplayName + '\'' +
                ", notificationEnable=" + notificationEnable +
                ", paymentType=" + paymentType +
                ", msisdn='" + msisdn + '\'' +
                ", tillNumber='" + tillNumber + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", depositPercentage=" + depositPercentage +
                ", orderTemplateType=" + orderTemplateType +
                ", newOrderTemplate='" + newOrderTemplate + '\'' +
                ", partialOrderPaymentTemplate='" + partialOrderPaymentTemplate + '\'' +
                ", fullOrderPaymentTemplate='" + fullOrderPaymentTemplate + '\'' +
                ", cancelOrderTemplate='" + cancelOrderTemplate + '\'' +
                ", deliverOrderTemplate='" + deliverOrderTemplate + '\'' +
                ", paymentReminderTemplate='" + paymentReminderTemplate + '\'' +
                ", newOrderEnable=" + newOrderEnable +
                ", partialOrderPaymentEnable=" + partialOrderPaymentEnable +
                ", fullOrderPaymentEnable=" + fullOrderPaymentEnable +
                ", cancelOrderEnable=" + cancelOrderEnable +
                ", deliverOrderEnable=" + deliverOrderEnable +
                ", paymentDue=" + paymentDue +
                ", baker=" + baker +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }
}