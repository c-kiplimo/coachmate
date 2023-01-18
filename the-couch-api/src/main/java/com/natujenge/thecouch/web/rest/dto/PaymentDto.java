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

    // order details to send to client
    public interface SessionView {
        Long getId();
        String getName();
    }

    // customer details to send to client
    public interface ClientView {
        Long getId();
        String getFirstName();
        String getMsisdn();
    }
    // baker details to send client
    public interface CoachView {
        Long getId();

        String getBusinessName();
    }
}
