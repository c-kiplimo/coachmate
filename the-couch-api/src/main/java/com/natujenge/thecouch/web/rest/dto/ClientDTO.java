package com.natujenge.thecouch.web.rest.dto;

import com.natujenge.thecouch.domain.enums.*;
import lombok.Data;

import java.time.LocalDateTime;


public class ClientDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String msisdn;
    private String email;


    private ClientType clientType;
    private ClientStatus clientStatus;
    private PaymentModeSubscription paymentMode;
    private  String profession;
    private  String physicalAddress;
    private String clientNumber;

    private String reason;
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
    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getFullName(){
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getMsisdn(){
        return msisdn;
    }
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public ClientType getClientType() {
        return clientType;
    }
    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public ClientStatus getClientStatus() {
        return clientStatus;
    }
    public void setClientStatus(ClientStatus clientStatus) {
        this.clientStatus = clientStatus;
    }
    public PaymentModeSubscription getPaymentMode() {
        return paymentMode;
    }
    public void setPaymentMode(PaymentModeSubscription paymentMode) {
        this.paymentMode = paymentMode;
    }
    public String getProfession() {
        return profession;
    }
    public void setProfession(String profession) {
        this.profession = profession;
    }
    public String getPhysicalAddress() {
        return physicalAddress;
    }
    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }
    public String getClientNumber() {
        return clientNumber;
    }
    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
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

    @Override
    public String toString() {
        return "ClientDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", msisdn='" + msisdn + '\'' +
                ", email='" + email + '\'' +
                ", clientType=" + clientType +
                ", clientStatus=" + clientStatus +
                ", paymentMode=" + paymentMode +
                ", profession='" + profession + '\'' +
                ", physicalAddress='" + physicalAddress + '\'' +
                ", clientNumber='" + clientNumber + '\'' +
                ", reason='" + reason + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }
}
