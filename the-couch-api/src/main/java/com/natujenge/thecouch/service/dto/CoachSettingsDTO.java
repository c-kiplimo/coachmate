package ke.natujenge.baked.service.dto;

import java.time.LocalDateTime;

public class BakerSettingsDTO {

    private Long id;
    private String logo;
    private BakerDTO baker;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public BakerDTO getBaker() {
        return baker;
    }

    public void setBaker(BakerDTO baker) {
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
        return "BakerSettings{" +
                "id=" + id +
                ", logo='" + logo + '\'' +
                ", baker=" + baker +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }
}
