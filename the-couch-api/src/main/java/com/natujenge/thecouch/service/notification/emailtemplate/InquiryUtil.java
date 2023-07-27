package com.natujenge.thecouch.service.notification.emailtemplate;


import com.natujenge.thecouch.web.rest.dto.InquiryDTO;

public class InquiryUtil {

    public static String getInquiryEmailBody(InquiryDTO inquiryDTO) {

        String phoneNumber="";
        if (inquiryDTO.getPhoneNumber() != null && !inquiryDTO.getPhoneNumber().trim().isEmpty()){
            phoneNumber = "    <p><b>Phone Number:</b> " + inquiryDTO.getPhoneNumber() + "</p>\n";
        }

        return  "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <title>Inquiry</title>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "</head>" +
                "<body>\n" +
                "    <p>Dear Support Team,</p>\n" +
                "    <p><b>Name:</b> " + inquiryDTO.getName() + "</p>\n" +
                "    <p><b>Email Address:</b> " + inquiryDTO.getEmail() + "</p>\n" +
                phoneNumber +
                "    <p><b>Message:</b> " + inquiryDTO.getMessage()+ "</p>\n" +
                "    <p>\n" +
                "        <span>Regards,</span>\n" +
                "        <br />\n" +
                "        <em>Support Team.</em>\n" +
                "    </p>\n" +
                "</body>" +
                "</html>";
    }
}
