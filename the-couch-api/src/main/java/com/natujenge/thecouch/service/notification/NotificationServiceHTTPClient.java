package com.natujenge.thecouch.service.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.natujenge.thecouch.config.Constants;
import com.natujenge.thecouch.domain.User;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class NotificationServiceHTTPClient {

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public String sendEmail(String destinationAddress, String subject, String content, boolean isHtml) {
        return this.sendEmail(
                Constants.DEFAULT_EMAIL_SOURCE_ADDRESS,
                destinationAddress,
                subject,
                content,
                isHtml
        );
    }

    public String sendEmail(
            String sourceAddress,
            String destinationAddress,
            String subject,
            String content,
            boolean isHtml
    ) {

        log.info(
                "Request to send email. sourceAddress : {}, destinationAddress : {}, subject : {}, isHtml : {} ",
                sourceAddress,
                destinationAddress,
                subject,
                isHtml
        );

        String referenceId = UUID.randomUUID().toString();

        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("destinationAddress", destinationAddress);
        messageMap.put("messageType", "EMAIL");
        messageMap.put("subject", subject);
        messageMap.put("message", content);
        messageMap.put("referenceId", referenceId);
        messageMap.put("isHtml", String.valueOf(isHtml));
        messageMap.put("sourceAddress", sourceAddress);

        try {
            String jsonRequest = objectMapper.writeValueAsString(messageMap);

            RequestBody requestBody = RequestBody.create(jsonRequest, MediaType.parse("application/json"));

            Request request = new Request.Builder().url(Constants.NOTIFICATION_URL).post(requestBody).build();
            try (Response ignored = okHttpClient.newCall(request).execute()) {
                return referenceId;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendSMS(String msisdn, String content, String referenceId) {
        this.sendSMS(Constants.DEFAULT_SMS_SOURCE_ADDRESS, msisdn, content, referenceId);
    }

    public void sendSMS(String sourceAddress, String msisdn, String content, String referenceId) {
        log.info("Request to send sms. sourceAddress : {}, msisdn : {}", sourceAddress, msisdn);

        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("destinationAddress", msisdn);
        messageMap.put("messageType", "SMS");
        messageMap.put("message", content);
        messageMap.put("referenceId", referenceId);
        messageMap.put("sourceAddress", sourceAddress);

        try {
            String jsonRequest = objectMapper.writeValueAsString(messageMap); //JSON.stringly() - object

            log.info(jsonRequest);

            RequestBody requestBody = RequestBody.create(jsonRequest,
                    MediaType.parse("application/json"));

            Request request = new Request.Builder().url(Constants.NOTIFICATION_URL).post(requestBody).build();
            try (Response ignored = okHttpClient.newCall(request).execute()) {}

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void sendConfirmationToken(String token,String action, User user){

//        String smsContent = "Use the following link to confirm your account \n" + Constants.API_ENDPOINT +
//                "/registration/confirm?token="+token;
        String smsContent;
        if (Objects.equals(action, "RESET")){
            smsContent = "Hello " + user.getFullName()+",\n Use this code to RESET" +
                    " your password: " + token + "\nYou can Ignore this If you didn't Initiate this action";
        }else{
            smsContent = "Hello " + user.getFullName()+",\n Use this code to confirm" +
                    " your phone number: " + token + "\nYou can Ignore this If you didn't Initiate this action";
        }

        // SHORTCODE
        String sourceAddress = Constants.DEFAULT_SMS_SOURCE_ADDRESS;
        String referenceId = "C-3452";
        String msisdn = user.getCoach().getMsisdn();
        String email = user.getCoach().getEmailAddress();
        log.info("About to send message to Coach content: {}, from: {}, to: {}, ref id {}",
                smsContent, sourceAddress, msisdn, referenceId);

        //send sms
        log.info("Sending notification to client");
        NotificationServiceHTTPClient notificationServiceHTTPClient = new NotificationServiceHTTPClient();
        notificationServiceHTTPClient.sendSMS(sourceAddress,msisdn,smsContent,referenceId);

        // send Email
    }
}
