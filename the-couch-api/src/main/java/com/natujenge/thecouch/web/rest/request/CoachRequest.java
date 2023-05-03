package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.Organization;
import lombok.Data;

@Data
public class CoachRequest {
    private String firstName;
    private String lastName;
    private String msisdn;
    private String email;
    private String password;
    private String token;
    private Long id;
    Organization organization;

}

