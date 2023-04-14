package ke.natujenge.baked.service.dto;

import java.time.LocalDateTime;

public class BakerSubscriptionDTO {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Long bakerId;
    private Long planId;
    private String planName;
    private BakerDTO  baker;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getBakerId() {
        return bakerId;
    }

    public void setBakerId(Long bakerId) {
        this.bakerId = bakerId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public BakerDTO getBaker() {
        return baker;
    }

    public void setBaker(BakerDTO baker) {
        this.baker = baker;
    }

    @Override
    public String toString() {
        return "BakerSubscriptionDTO{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", bakerId=" + bakerId +
                ", planId=" + planId +
                ", planName=" + planName +
                ", baker=" + baker +
                '}';
    }
}
