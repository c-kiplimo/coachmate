package com.natujenge.thecouch.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
@Table(name = "tbl_coach_subscription")
@Entity
public class CoachSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date", length = 35)
    private LocalDateTime startDate;
    @Column(name = "end_date", length = 35)
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private User coach;
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public User getCoach() {
        return coach;
    }

    public void setCoach(User coach) {
        this.coach = coach;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return "CoachSubscription{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", coach=" + coach +
                ", organization=" + organization +
                ", plan=" + plan +
                '}';
    }
}
