package ke.natujenge.baked.service.dto;

import ke.natujenge.baked.domain.Baker;
import ke.natujenge.baked.domain.Customer;
import ke.natujenge.baked.domain.enums.ModeOfPayment;
import ke.natujenge.baked.domain.enums.PaymentType;

import java.time.LocalDateTime;

public class PaymentDTO {

    private Long id;
    private String paymentRef;
    private String extPaymentRef;
    private ModeOfPayment modeOfPayment;
    private PaymentType paymentType;
    private Float amount;
    private Float balanceAfter;
    private Float overPayment;
    private String narration;
    private String paidOn;
    private String trxMessage;
    private OrderDTO order;
    private CustomerDTO customer;
    private BakerDTO baker;
    private LocalDateTime paidAt;
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

    public String getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }

    public String getExtPaymentRef() {
        return extPaymentRef;
    }

    public void setExtPaymentRef(String extPaymentRef) {
        this.extPaymentRef = extPaymentRef;
    }

    public ModeOfPayment getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(ModeOfPayment modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(Float balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public Float getOverPayment() {
        return overPayment;
    }

    public void setOverPayment(Float overPayment) {
        this.overPayment = overPayment;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getPaidOn() {
        return paidOn;
    }

    public void setPaidOn(String paidOn) {
        this.paidOn = paidOn;
    }

    public String getTrxMessage() {
        return trxMessage;
    }

    public void setTrxMessage(String trxMessage) {
        this.trxMessage = trxMessage;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public BakerDTO getBaker() {
        return baker;
    }

    public void setBaker(BakerDTO baker) {
        this.baker = baker;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
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

    @Override
    public String toString() {
        return "PaymentDTO{" +
                "id=" + id +
                ", paymentRef='" + paymentRef + '\'' +
                ", extPaymentRef='" + extPaymentRef + '\'' +
                ", modeOfPayment=" + modeOfPayment +
                ", paymentType=" + paymentType +
                ", amount=" + amount +
                ", balanceAfter=" + balanceAfter +
                ", overPayment=" + overPayment +
                ", narration='" + narration + '\'' +
                ", paidOn='" + paidOn + '\'' +
                ", trxMessage='" + trxMessage + '\'' +
                ", order=" + order +
                ", customer=" + customer +
                ", baker=" + baker +
                ", paidAt=" + paidAt +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }
}
