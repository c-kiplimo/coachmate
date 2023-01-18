package com.natujenge.thecouch.util;

import com.natujenge.thecouch.domain.Constants;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.service.notification.NotificationServiceHTTPClient;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;

@Log4j2
public class NotificationHelper {
    public static void sendConfirmationToken(String token,String action, User user){

//        String smsContent = "Use the following link to confirm your account \n" + Constants.API_ENDPOINT +
//                "/registration/confirm?token="+token;
        String smsContent;
        if (Objects.equals(action, "RESET")){
            smsContent = "Hello " + user.getFirstName()+",\n Use this code to RESET" +
                    " your password: " + token + "\nYou can Ignore this If you didn't Initiate this action";
        }else{
            smsContent = "Hello " + user.getFirstName()+",\n Use this code to confirm" +
                    " your phone number: " + token + "\nYou can Ignore this If you didn't Initiate this action";
        }

        // SHORTCODE
        String sourceAddress = Constants.DEFAULT_SMS_SOURCE_ADDRESS;
        String referenceId = "C-23342";
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
