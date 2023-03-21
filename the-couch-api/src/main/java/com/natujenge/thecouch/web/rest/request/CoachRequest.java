package com.natujenge.thecouch.web.rest.request;

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
        private Long coachId;
        private Long orgIdId;
        private String createdBy;

    }

