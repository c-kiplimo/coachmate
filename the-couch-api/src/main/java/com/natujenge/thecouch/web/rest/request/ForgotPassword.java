package com.natujenge.thecouch.web.rest.request;

import lombok.Data;

@Data
public class ForgotPassword {
    public String msisdn;
    public String otp;
    public String password;
}
