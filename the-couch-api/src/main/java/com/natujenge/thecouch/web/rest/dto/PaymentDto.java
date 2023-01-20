package com.natujenge.thecouch.web.rest.dto;

import java.time.LocalDateTime;

public interface PaymentDto {

    Long getId();
    String getExtPaymentRef() ;
    Float getAmount();
    Float getBalanceAfter();
    Float getOverPayment();

    String getNarration();

    LocalDateTime getPaidAt();

    SessionView getSession();

    ClientView getClient();

    CoachView getCoach();

    // session details to send to client
    public interface SessionView {
        Long getId();
        String getName();
    }

    // client details to send to coach
    public interface ClientView {
        Long getId();
        String getFirstName();
        String getMsisdn();
    }
    // coach details to send
    public interface CoachView {
        Long getId();

        String getBusinessName();
    }
}
