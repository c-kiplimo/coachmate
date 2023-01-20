package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.enums.ClientType;
import com.natujenge.thecouch.domain.enums.PaymentMode;
import lombok.Data;

@Data
public class ClientRequest {
    private String firstName;
    private String lastName;
    private ClientType clientType;
    private String msisdn;
    private String email;
    private String physicalAddress;
    private String profession;
    private PaymentMode paymentMode;
    private String reason;
    private String password;
    private String token;


}
