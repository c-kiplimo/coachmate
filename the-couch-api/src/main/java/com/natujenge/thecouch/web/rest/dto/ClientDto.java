package com.natujenge.thecouch.web.rest.dto;

import com.natujenge.thecouch.domain.enums.ClientStatus;

import java.time.LocalDateTime;

public interface ClientDto {
    Long getId();
    String getFirstName();
    String getLastName();
    String getMsisdn();
    String getEmail();
    String getReason();
    ClientStatus getStatus();
    CoachView getCoach();

    String getPhysicalAddress();

    public interface  CoachView {
        Long getId();
        String getBusinessName();
    }
}
