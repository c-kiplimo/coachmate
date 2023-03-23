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


    // DEFAULT TEMPLATES FOR ALL USERS
    public static final String DEFAULT_NEW_CONTRACT_SMS_TEMPLATE = "Dear $client_name. Your contract $coaching_topic " +
            ", starting  on $start_date to $end_date , has been created. Please login to your account to sign the contract. Thank you. $business_name";
    public static final String DEFAULT_NEW_CONTRACT_EMAIL_TEMPLATE = "Dear $client_name. Your contract $coachingTopic " +
            " starting  on $startDate to $endDate , has been created. Please login to your account to sign the contract. Thank you. $business_name";

    public static final String DEFAULT_PARTIAL_BILL_PAYMENT_TEMPLATE =  "Dear $client_name. We have received " +
            "your payment of amount $amountDeposited . Your wallet balance " +
            "balance is $amountBilled." +
            " Thank you. $business_name";

    public static final String DEFAULT_NEW_ORGANIZATION_COACH_CONTRACT_SMS_TEMPLATE = "Dear $coach_name. Your contract is starting  on $start_date to $end_date , has been created. Please login to your account to sign the contract. Thank you. $business_name";
    public static final String DEFAULT_NEW_ORGANIZATION_CLIENT_CONTRACT_SMS_TEMPLATE = "Dear $client_name. Your contract is starting  on $start_date to $end_date , has been created. Please login to your account to sign the contract. Thank you. $business_name";

    public static final String FULL_BILL_PAYMENT_TEMPLATE =  "Dear $client_name. We have received " +
            "your payment of amount $amountDeposited " +
            "Outstanding balance $amountBilled . Overpayment $walletBalance . Thank you. $business_name";

    public static final String CANCEL_SESSION_TEMPLATE = "Dear $client_name. Your session $sessionName " +
            "has been cancelled successfully. Thank you. $business_name.";
    public static final String RESCHEDULE_SESSION_TEMPLATE = "Dear $client_name. Your session $sessionName " +
            "has been rescheduled successfully. Thank you. $business_name.";

    public static final String CONDUCTED_SESSION_TEMPLATE = "Dear $client_name. Your session $sessionName has been" +
            " conducted" +
            " Successfully. We look forward to your feedback. Thank you. $business_name.";


    public static final String DEFAULT_PAYMENT_REMINDER_TEMPLATE =  "Dear $client_name. You have an outstanding" +
            "balance of $amountBilled. Thank you. $business_name";


}