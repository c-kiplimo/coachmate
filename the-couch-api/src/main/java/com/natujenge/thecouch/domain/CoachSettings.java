package ke.natujenge.baked.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
    * Logo: Image? Link to logo?
 */

@Table(name = "tbl_baker_settings")
@Entity
public class BakerSettings {
    @SequenceGenerator(
            name = "baker_settings_sequence",
            sequenceName = "baker_settings_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "baker_settings_sequence")
    @Id
    private Long id;
    private String logo;
    @OneToOne // Baker could have one or multiple locations
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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
