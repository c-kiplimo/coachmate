package com.natujenge.thecouch.service.dto;

import com.natujenge.thecouch.domain.enums.MPesaPaymentType;
import com.natujenge.thecouch.domain.enums.PaymentType;

import java.time.LocalDateTime;

public class PaymentDetailsDTO {
    private Long id;
    private PaymentType paymentType;
    private MPesaPaymentType mpesaPaymentType;
    private String msisdn; //send money & pochi
    private String tillNumber;//buy goods
    private String bankName; //bank
    private String bankBranchName; //bank
    private String accountName; //bank
    private String businessNumber; //pay bill
    private String accountNumber; // bank & pay bill
    private float depositPercentage;
    private CoachDTO coach;
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

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public MPesaPaymentType getMpesaPaymentType() {
        return mpesaPaymentType;
    }

    public void setMpesaPaymentType(MPesaPaymentType mpesaPaymentType) {
        this.mpesaPaymentType = mpesaPaymentType;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public float getDepositPercentage() {
        return depositPercentage;
    }

    public void setDepositPercentage(float depositPercentage) {
        this.depositPercentage = depositPercentage;
    }

    public CoachDTO getCoach() {
        return coach;
    }

    public void setCoach(CoachDTO baker) {
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

    @Override
    public String toString() {
        return "BakerPaymentDetailsDTO{" +
                "id=" + id +
                ", paymentType=" + paymentType +
                ", mpesaPaymentType=" + mpesaPaymentType +
                ", msisdn='" + msisdn + '\'' +
                ", tillNumber='" + tillNumber + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankBranchName='" + bankBranchName + '\'' +
                ", accountName='" + accountName + '\'' +
                ", businessNumber='" + businessNumber + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", depositPercentage=" + depositPercentage +
                ", coach=" + coach +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }
}
