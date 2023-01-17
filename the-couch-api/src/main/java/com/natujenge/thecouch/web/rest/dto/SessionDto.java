package com.natujenge.thecouch.web.rest.dto;

import com.natujenge.thecouch.domain.enums.*;
import lombok.Data;
import org.hibernate.type.StringNVarcharType;
import org.springframework.boot.context.event.SpringApplicationEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
public interface SessionDto {
    Long getId();
    String getName();
    SessionType getSessionType();
    SessionStatus getSessionStatus();
    String getNotes();
    String getFeedback();
    LocalDate getSessionDate();
    LocalDateTime getSessionStartTime();
    LocalDateTime getSessionEndTime();
    SessionVenue getSessionVenue();
    PaymentCurrency getPaymentCurrency();
    String getAmountPaid();

    ClientView getClient();

    ContractView getContract();

    interface ClientView{
        Long getId();

        String getName();

        ClientType getType();

        String getMsisdn() ;
    }

    interface ContractView{
        Long getId();

        CoachingCategory getCoachingCategory();
        String getCoachingTopic();
    }
}
