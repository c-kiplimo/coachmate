package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.enums.ModeOfPayment;
import com.natujenge.thecouch.domain.enums.PaymentCurrency;
import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

@Data
public class PaymentRequest {
    public String extPaymentRef;
    public ModeOfPayment modeOfPayment;
    public Float amount;
    public String description;
    public PaymentCurrency paymentCurrency;
    public Long clientId;

    // notification clients override choice
    public boolean sendNotification;
}
