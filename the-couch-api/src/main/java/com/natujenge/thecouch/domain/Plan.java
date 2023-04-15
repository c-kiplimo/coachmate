package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.PlanType;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name", length=100)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name="type", length=15)
    private PlanType type;
    @Column(name="features", length=500)
    private String features;
    @Column(name="permissions", length=500)
    private String permissions;
    @Column(name="cost")
    private float cost;
    @Column(name="duration")
    private Integer duration;
    @Column(name="valid_from", length=35)
    private LocalDate validFrom;
    @Column(name="valid_to", length=35)
    private LocalDate validTo;
    @Column(name="is_default")
    private boolean isDefault;
    @Column(name="preloaded_amount", columnDefinition = "float default 0")
    private float preloadedAmount;
    @Column(name="notification_message", length=500)
    private String notificationMessage;
    @Column(name="active")
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlanType getType() {
        return type;
    }

    public void setType(PlanType type) {
        this.type = type;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public float getPreloadedAmount() {
        return preloadedAmount;
    }

    public void setPreloadedAmount(float preloadedAmount) {
        this.preloadedAmount = preloadedAmount;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", features='" + features + '\'' +
                ", permissions='" + permissions + '\'' +
                ", cost=" + cost +
                ", duration=" + duration +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                ", isDefault=" + isDefault +
                ", preloadedAmount='" + preloadedAmount + '\'' +
                ", notificationMessage='" + notificationMessage + '\'' +
                ", active=" + active +
                '}';
    }
}
