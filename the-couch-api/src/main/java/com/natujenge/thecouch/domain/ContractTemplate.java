package com.natujenge.thecouch.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_contract_templates")
@Entity
public class ContractTemplates {
    @SequenceGenerator(
            name = "contract_templates_sequence",
            sequenceName = "contract_templates_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_templates_sequence")
    private Long id;
    private String servicesTemplate;
    private String practiceTemplate;
    private String terms_and_conditionsTemplate;
    private String privacyPolicyTemplate;
    private String notesTemplate;
    @ManyToOne
    @JoinColumn(name="coach_id")
    Coach coach;
    @ManyToOne
    @JoinColumn(name="organization_id")
    Organization organization;

}
