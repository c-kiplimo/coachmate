package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.domain.Organization;
import lombok.Data;

@Data
public class ContractTemplatesRequest {
    private String servicesTemplate;
    private String practiceTemplate;
    private String terms_and_conditionsTemplate;
    private String privacyPolicyTemplate;
    private String notesTemplate;
    Coach coach;
    Organization organization;
}
