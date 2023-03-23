package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.SessionSchedules;
import com.natujenge.thecouch.domain.enums.PaymentCurrency;
import com.natujenge.thecouch.domain.enums.SessionType;
import com.natujenge.thecouch.domain.enums.SessionVenue;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SessionRequest {
    public String sessionDuration;
    public String name;
    public SessionType sessionType;
    public String notes;

    public SessionSchedules sessionSchedules;

    public String sessionNumber;
    public LocalDate sessionDate;

    public SessionVenue sessionVenue;
    public PaymentCurrency paymentCurrency;
    public Float amountPaid;
    public Long clientId;
}
