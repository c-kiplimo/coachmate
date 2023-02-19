package com.natujenge.thecouch.domain;

import com.natujenge.thecouch.domain.enums.CoachingCategory;
import com.natujenge.thecouch.domain.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_contracts")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private CoachingCategory coachingCategory;
    private String coachingTopic;
    private LocalDate startDate;
    private LocalDate endDate;


    private     Float individualFeesPerSession;
    private Float groupFeesPerSession;
    private Integer noOfSessions;
    private Float amountDue;

    @Nullable
    @ManyToOne
    @JoinColumn(name="organization_id")

 private Organization organization;

    // relations
    @ManyToOne
    @JoinColumn(name="client_id")
    Client client;

    @ManyToOne
    @JoinColumn(name="coach_id")
    Coach coach;

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", coachingCategory=" + coachingCategory +
                ", coachingTopic='" + coachingTopic + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", individualFeesPerSession=" + individualFeesPerSession +
                ", groupFeesPerSession=" + groupFeesPerSession +
                ", noOfSessions=" + noOfSessions +
                ", amountDue=" + amountDue +
                ", organization=" + organization +
                ", client=" + client +
                ", coach=" + coach +
                '}';
    }
}
