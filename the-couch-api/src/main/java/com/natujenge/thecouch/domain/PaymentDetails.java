package ke.natujenge.baked.domain;

import ke.natujenge.baked.domain.enums.MPesaPaymentType;
import ke.natujenge.baked.domain.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "tbl_baker_payment_details")
@Entity
public class BakerPaymentDetails {
    @SequenceGenerator(
            name = "baker_payment_details_sequence",
            sequenceName = "baker_payment_details_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "baker_payment_details_sequence")
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
    @ManyToOne // Baker could have one or multiple payment details
    @JoinColumn(
            nullable = false,
            name = "baker_id"
    )
    private Baker baker;

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

    public Baker getBaker() {
        return baker;
    }

    public void setBaker(Baker baker) {
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
                ", baker=" + baker +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }
}
