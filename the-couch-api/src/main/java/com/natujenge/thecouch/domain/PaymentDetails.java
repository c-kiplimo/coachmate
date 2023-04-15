package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.MPesaPaymentType;
import com .natujenge.thecouch.domain.enums.PaymentType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "tbl_payment_details")
@Entity
public class PaymentDetails {
    @SequenceGenerator(
            name = "payment_details_sequence",
            sequenceName = "payment_details_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "payment_details_sequence")
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    @Enumerated(EnumType.STRING)
    private MPesaPaymentType mpesaPaymentType;
    private String msisdn;
    private String tillNumber;
    private String bankName;
    private String bankBranchName;
    private String accountName;
    private String businessNumber;
    private String accountNumber;
    private float depositPercentage;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "coach_id"
    )
    private Coach coach;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "organization_id"
    )
    private Organization organization;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
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

    public Coach getCoach() {
        return coach;
    }

    public void setBaker(Coach coach) {
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
        return "BakerPaymentDetails{" +
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
                ", coach =" + coach +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }
}
