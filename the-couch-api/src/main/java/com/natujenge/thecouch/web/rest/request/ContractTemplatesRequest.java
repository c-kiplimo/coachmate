package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.User;
import lombok.Data;

@Data
public class ContractTemplatesRequest {
    private String servicesTemplate;
    private String practiceTemplate;
    private String terms_and_conditionsTemplate;
    private String privacyPolicyTemplate;
    private String notesTemplate;
   private User coach;
    Organization organization;
}
