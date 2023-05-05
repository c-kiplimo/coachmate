package com.natujenge.thecouch.web.rest.dto;

import com.natujenge.thecouch.domain.enums.ClientType;
import com.natujenge.thecouch.domain.enums.CoachingCategory;

import java.time.LocalDate;

public interface ContractDto {
    Float getAmountDue();
    CoachingCategory getCoachingCategory();
    String getCoachingTopic();
    LocalDate getEndDate();
    Float getGroupFeesPerSession();
    Float getIndividualFeesPerSession();
    Integer getNoOfSessions();
    Long getOrgId();
    LocalDate getStartDate();

    interface ClientView{
        Long getId();
        String getFullName();
        ClientType getClientType();
        String getMsisdn() ;
    }
    interface CoachView{
        Long getId();
    }

}
