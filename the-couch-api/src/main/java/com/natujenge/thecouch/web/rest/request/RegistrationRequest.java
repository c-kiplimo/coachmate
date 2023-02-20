package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.enums.UserRole;
import lombok.Data;

@Data
public class RegistrationRequest {
    private Long id;
    private final String fullName;
    private String firstName;
    private String lastName;
    private String businessName;
    private String msisdn;
    private final String email;
    private final String password;
    private UserRole userRole;
    private Organization organization;


}
