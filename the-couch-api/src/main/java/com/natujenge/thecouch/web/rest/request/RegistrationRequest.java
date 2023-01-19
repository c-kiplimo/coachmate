package com.natujenge.thecouch.web.rest.request;

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
}
