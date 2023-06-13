package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.SessionTemplateType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_notification_templates")
@Entity
public class NotificationTemplate {
    @SequenceGenerator(
            name = "notification_template_sequence",
            sequenceName = "notification_template_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "notification_template_sequence")
    @Id
    private Long id;
    private SessionTemplateType sessionTemplateType;
    private boolean enable = true;
    private String email;
    private String sms;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private String createdBy;
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;
}
