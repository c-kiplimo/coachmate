package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.ModeOfPayment;
import com.natujenge.thecouch.domain.enums.PaymentCurrency;
import com.natujenge.thecouch.service.ClientBillingAccountService;
import lombok.Data;

@Data
public class PaymentRequest {
    public static ClientBillingAccountService clientBillingAccountService;
    public String extPaymentRef;
    public ModeOfPayment modeOfPayment;
    public Float amount;
    public String description;
    public PaymentCurrency paymentCurrency;
    public Long clientId;
    public Long coachId;
    public Long organizationId;
    User coach;
    User client;
    Organization organization;
    public String createdBy;
    // notification clients override choice
    public boolean sendNotification;

}
