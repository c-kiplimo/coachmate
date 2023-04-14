package ke.natujenge.baked.service.dto;

import ke.natujenge.baked.domain.enums.MPesaPaymentType;
import ke.natujenge.baked.domain.enums.PaymentType;

public class OnBoardBakerDTO {
    private Long bakerId;
    private String depositPercentage;
    private String newOrderTemplate;
    private String partialOrderPaymentTemplate;
    private String fullOrderPaymentTemplate;
    private String deliverOrderTemplate;
    private String cancelOrderTemplate;

    private String filename;

    private String county;
    private String physicalAddress;
    private String postalAddress;

    private PaymentType paymentType;
    private MPesaPaymentType mpesaPaymentType;
    private String tillNumber;
    private String msisdn;
    private String businessNumber;
    private String accountNumber;

    public Long getBakerId() {
        return bakerId;
    }

    public void setBakerId(Long bakerId) {
        this.bakerId = bakerId;
    }

    public String getDepositPercentage() {
        return depositPercentage;
    }

    public void setDepositPercentage(String depositPercentage) {
        this.depositPercentage = depositPercentage;
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

    public String getDeliverOrderTemplate() {
        return deliverOrderTemplate;
    }

    public void setDeliverOrderTemplate(String deliverOrderTemplate) {
        this.deliverOrderTemplate = deliverOrderTemplate;
    }

    public String getCancelOrderTemplate() {
        return cancelOrderTemplate;
    }

    public void setCancelOrderTemplate(String cancelOrderTemplate) {
        this.cancelOrderTemplate = cancelOrderTemplate;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
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

    public String getTillNumber() {
        return tillNumber;
    }

    public void setTillNumber(String tillNumber) {
        this.tillNumber = tillNumber;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
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

    @Override
    public String toString() {
        return "OnBoardBakerDTO{" +
                "bakerId=" + bakerId +
                ", depositPercentage='" + depositPercentage + '\'' +
                ", newOrderTemplate='" + newOrderTemplate + '\'' +
                ", partialOrderPaymentTemplate='" + partialOrderPaymentTemplate + '\'' +
                ", fullOrderPaymentTemplate='" + fullOrderPaymentTemplate + '\'' +
                ", deliverOrderTemplate='" + deliverOrderTemplate + '\'' +
                ", cancelOrderTemplate='" + cancelOrderTemplate + '\'' +
                ", filename='" + filename + '\'' +
                ", county='" + county + '\'' +
                ", physicalAddress='" + physicalAddress + '\'' +
                ", postalAddress='" + postalAddress + '\'' +
                ", paymentType=" + paymentType +
                ", mpesaPaymentType=" + mpesaPaymentType +
                ", tillNumber='" + tillNumber + '\'' +
                ", msisdn='" + msisdn + '\'' +
                ", businessNumber='" + businessNumber + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                '}';
    }
}
