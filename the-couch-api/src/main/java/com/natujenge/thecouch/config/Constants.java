package com.natujenge.thecouch.config;

public class Constants {
    public static final long   ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24;
    public static final String SIGNING_KEY                   = "20201LIPACHATQNATUJENGEQAZ2wsx4rfvBHUZ2wsx4rfvBHU*";
    public static final String TOKEN_PREFIX                  = "Bearer ";
    public static final String HEADER_STRING                 = "Authorization";

    // Portal URL
    public static final String API_ENDPOINT                 = "http://51.15.243.105:4200";

    public static final String NOTIFICATION_URL = "http://51.15.233.87:15432/message/queue";
    public static final String DEFAULT_EMAIL_SOURCE_ADDRESS = "alerts@meliora.tech";

    public static final String DEFAULT_SUPPORT_EMAIL_SOURCE_ADDRESS = "kiplimocollins855@gmail.com";
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

    public static final String UPLOADS_FOLDER ="/apps/uploads";
    public static final String DEFAULT_SERVICES_TEMPLATE = "The Coach and Client agree to engage in a coaching engagement through\n" +
            "              in-person, email or virtual meetings as agreed by both parties. The\n" +
            "              Coach will be available to the Client by email, Zoom or phone in\n" +
            "              between scheduled meetings as defined by the Coach. The Coach may also\n" +
            "              be available for additional time, on the Client’s request during the\n" +
            "              term of this contract (engaging in other Client-related services\n" +
            "              outside of coaching hours to review goals, read reports, etc.). Time\n" +
            "              outside the agreed bracket as outlined will be at the rate of USD 50\n" +
            "              for every 30-minute time block.";
    public static final String DEFAULT_TERMS_AND_CONDITIONS_TEMPLATE = "Please read the terms and conditions carefully. " +
            "By using this service, you agree to be bound by the terms and conditions of this agreement. " +
            "If you do not agree to these terms, please do not use this service. " +
            "We reserve the right, at our sole discretion, to modify or replace these Terms at any time. " +
            "If a revision is material we will try to provide at least 30 days notice prior to any new terms taking effect. " +
            "What constitutes a material change will be determined at our sole discretion. " +
            "By continuing to access or use our service after those revisions become effective, " +
            "you agree to be bound by the revised terms. If you do not agree to the new terms, please stop using the service. " +
            "The latest version of the Terms and Conditions will be posted on this page. " +
            "You are responsible for reviewing the Terms and Conditions periodically. " +
            "Your continued use of the Service following the posting of revised Terms and Conditions means that you accept and agree to the changes. " +
            "If you do not agree to the new terms, please stop using the service. " +
            "The latest version of the Terms and Conditions will be posted on this page. " +
            "You are responsible for reviewing the Terms and Conditions periodically. " +
            "Your continued use of the Service following the posting of revised Terms and Conditions means that you accept and agree to the changes. " +
            "If you do not agree to the new terms, please stop using the service. " +
            "The latest version of the Terms and Conditions will be posted on this page. " +
            "You are responsible for reviewing the Terms and Conditions periodically. " +
            "Your continued use of the Service following the posting of revised Terms and Conditions means that you accept and agree to the changes. " +
            "If you do not agree to the new terms, please stop using the service. " +
            "The latest version of the Terms and Conditions will be posted on this page. " +
            "You are responsible for reviewing the Terms and Conditions periodically. " +
            "Your continued use of the Service following the posting of revised Terms and Conditions means that you accept and agree to the changes. " +
            "If you do not agree to the new terms, please stop using the service. ";
    public static final String DEFAULT_PRACTICE_TEMPLATE = "Fully understand and agree that I am fully responsible for my" +
            " physical, mental and emotional well-being during my coaching" +
            "sessions, including my choices and decisions. I am aware that I" +
            "can choose to discontinue coaching by giving a 14 days’ notice." + "Understand that Coaching is partnership (defined as an alliance,\n" +
            " not a legal business partnership) between the Coach and the" +
            " Client. That it is a thought-provoking and creative process that" +
            "Inspires the Client to maximize personal and professional" +
            " potential." + "              Understand that Coaching is a Professional–Client relationship I\n" +
            "have with my Coach that is designed to facilitate the\n" +
            "creation/development of personal, professional or business goals\n" +
            " and to develop and carry out a strategy/plan for achieving those\n" +
            " goals." + "             Understand that coaching is a comprehensive process that may\n" +
            "              involve all areas of my life, including work, finances, health,\n" +
            "              relationships, education and recreation. I acknowledge that\n" +
            "              deciding how to handle these issues, incorporate coaching into\n" +
            "              those areas, and implement my choices is exclusively my\n" +
            "              responsibility.";
    public static final String DEFAULT_NOTE_TEMPLATE = "ADD ANY OTHER INFORMATION HERE";

}