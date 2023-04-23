package com.natujenge.thecouch.service.dto;

import com.natujenge.thecouch.domain.Organization;
import com .natujenge.thecouch.domain.enums.ModeOfPayment;
import com.natujenge.thecouch.domain.enums.PaymentType;
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
    private CoachDTO coach;
    private OrganizationDTO organization;
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

    public CoachDTO getCoach() {
        return coach;
    }

    public void setCoach(CoachDTO coach) {
        this.coach = coach;
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
                ", coach=" + coach +
                ", paidAt=" + paidAt +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }
}
