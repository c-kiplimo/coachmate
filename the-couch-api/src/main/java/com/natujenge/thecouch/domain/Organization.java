
package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.OrgStatus;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table(name = "tbl_organizations")
@Entity
public class Organization {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    private Long id;
    private String orgName;
    private String msisdn;
    private String email;
    private Long superCoachId; //user id

    @Enumerated(EnumType.STRING)
    private OrgStatus status;

    //Management details
    @CreationTimestamp
    private LocalDateTime createdAt;
    private String createdBy;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;

    //Relations
    @ManyToOne
    private User user;

    //Relations
//    @OneToMany
//    private List<Client> clients;
//
//    @OneToMany
//    private List<Coach> coaches;
}