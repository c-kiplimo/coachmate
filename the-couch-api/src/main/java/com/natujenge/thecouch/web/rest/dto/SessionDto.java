package com.natujenge.thecouch.web.rest.dto;

import com.natujenge.thecouch.domain.SessionSchedules;
import com.natujenge.thecouch.domain.enums.*;
import lombok.Data;
import org.hibernate.type.StringNVarcharType;
import org.springframework.boot.context.event.SpringApplicationEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
public interface SessionDto {
    Long getId();
    Long getCoachId();
    String getName();
    String getSessionDate();
    LocalDate getSessionDuration();
    String getSessionStartTime();
    String getSessionEndTime();
    String getSessionNumber();
    SessionType getSessionType();
    SessionStatus getSessionStatus();
    String getNotes();
    String getFeedback();

    SessionSchedules getSessionSchedules();


    SessionVenue getSessionVenue();
    PaymentCurrency getPaymentCurrency();
    String getAmountPaid();

    ClientView getClient();
    CoachView getCoach();
    ContractView getContract();

    interface ClientView{
        Long getId();

        String getFullName();
        ClientType getClientType();

        String getMsisdn() ;

    }
    interface CoachView{
        Long getId();
    }

    interface ContractView{
        Long getId();

        CoachingCategory getCoachingCategory();
        String getCoachingTopic();
    }
}
