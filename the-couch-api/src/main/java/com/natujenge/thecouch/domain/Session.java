package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.SessionStatus;
import com.natujenge.thecouch.domain.enums.SessionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@Table(name = "tbl_sessions")

public class Session {

    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private Integer session_no;
    private String name;

    @Enumerated(EnumType.STRING)
    private SessionType type;

    private String details;
    private String notes;
    private String amount_paid;
    private String session_date;
    private String delivery_date;
    private String delivered_at;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    private String reason;
    private String session_venue;
    private String attachments;
    private String client_id;
    private String coach_id;
    private String goals;
    private String feedback;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;

}
