package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.enums.ModeOfPayment;
import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

@Data
public class PaymentRequest {
    public String extPaymentRef;
    public ModeOfPayment modeOfPayment;
    public Float amount;
    public String narration;
    private Long sessionId;

    // notification clients override choice
    public boolean sendNotification;
}
