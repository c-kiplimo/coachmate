package ke.natujenge.baked.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ke.natujenge.baked.domain.Baker;
import ke.natujenge.baked.domain.enums.WalletStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

public class BakerWalletDTO {

    private Long id;
    private float currentBalance;
    private float reservedBalance;
    private float creditLimit;
    private WalletStatus status;
    private String mac;
    private String currency;
    private LocalDateTime lastTrxTime;
    private Long lastTrxId;
    private String code;
    private Long bakerId;

    private BakerDTO baker;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(float currentBalance) {
        this.currentBalance = currentBalance;
    }

    public float getReservedBalance() {
        return reservedBalance;
    }

    public void setReservedBalance(float reservedBalance) {
        this.reservedBalance = reservedBalance;
    }

    public float getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(float creditLimit) {
        this.creditLimit = creditLimit;
    }

    public WalletStatus getStatus() {
        return status;
    }

    public void setStatus(WalletStatus status) {
        this.status = status;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getLastTrxTime() {
        return lastTrxTime;
    }

    public void setLastTrxTime(LocalDateTime lastTrxTime) {
        this.lastTrxTime = lastTrxTime;
    }

    public Long getLastTrxId() {
        return lastTrxId;
    }

    public void setLastTrxId(Long lastTrxId) {
        this.lastTrxId = lastTrxId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getBakerId() {
        return bakerId;
    }

    public void setBakerId(Long bakerId) {
        this.bakerId = bakerId;
    }

    public BakerDTO getBaker() {
        return baker;
    }

    public void setBaker(BakerDTO baker) {
        this.baker = baker;
    }

    @Override
    public String toString() {
        return "BakerWallet{" +
                "id=" + id +
                ", currentBalance=" + currentBalance +
                ", reservedBalance=" + reservedBalance +
                ", creditLimit=" + creditLimit +
                ", status='" + status + '\'' +
                ", mac='" + mac + '\'' +
                ", currency='" + currency + '\'' +
                ", lastTrxTime='" + lastTrxTime + '\'' +
                ", lastTrxId=" + lastTrxId +
                ", bakerId=" + bakerId +
                ", baker=" + baker +
                '}';
    }
}
