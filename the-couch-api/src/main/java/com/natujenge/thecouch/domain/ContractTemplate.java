package com.natujenge.thecouch.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_contract_templates")
@Entity
public class ContractTemplate {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(length = 5000, columnDefinition = "text")
    private String servicesTemplate;
    @Column(length = 5000, columnDefinition = "text")
    private String practiceTemplate;
    @Column(length = 5000, columnDefinition = "text")
    private String terms_and_conditionsTemplate;
    @Column(length = 5000, columnDefinition = "text")
    private String privacyPolicyTemplate;
    @Column(length = 5000, columnDefinition = "text")
    private String notesTemplate;
    @ManyToOne
    @JoinColumn(name="coach_id")
    User user;
    @ManyToOne
    @JoinColumn(name="organization_id")
    Organization organization;

}
