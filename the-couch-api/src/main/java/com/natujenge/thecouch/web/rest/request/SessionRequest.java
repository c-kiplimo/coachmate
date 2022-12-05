package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.enums.PaymentCurrency;
import com.natujenge.thecouch.domain.enums.SessionType;
import com.natujenge.thecouch.domain.enums.SessionVenue;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SessionRequest {
    public String sessionDuration;
    public String name;
    public SessionType type;
    public LocalDate sessionDate;
    public SessionVenue sessionVenue;
    public PaymentCurrency paymentCurrency;
    public String amountPaid;
}
