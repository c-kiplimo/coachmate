package com.natujenge.thecouch.domain;

public class Constants {
    public static final long   ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24;

    public static final String SIGNING_KEY                   = "20201LIPACHATQNATUJENGEQAZ2wsx4rfvBHUZ2wsx4rfvBHU*";
    public static final String TOKEN_PREFIX                  = "Bearer ";
    public static final String HEADER_STRING                 = "Authorization";

    // Portal URL
    public static final String API_ENDPOINT                 = "http://51.15.243.105:4200";

    public static final String NOTIFICATION_URL = "http://51.15.233.87:15432/message/queue";
    public static final String DEFAULT_EMAIL_SOURCE_ADDRESS = "alerts@meliora.tech";
    public static final String DEFAULT_SMS_SOURCE_ADDRESS = "SMSAfrica";


}
