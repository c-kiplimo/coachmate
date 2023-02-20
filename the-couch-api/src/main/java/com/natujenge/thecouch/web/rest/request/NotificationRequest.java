package com.natujenge.thecouch.web.rest.request;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import lombok.Data;

@Data
public class NotificationRequest {
    public NotificationMode notificationMode;
    public String subject;
    public String srcAddress;
    public String dstAddress;
    public String content;
    public String sendReason;
    public String currency;
    public Long sessionId;
    public Long clientId;
    private Long contractId;
    private Long orgIdId;
    private Long coach;
    public Long paymentId;

    public void setNotificationMode(NotificationMode notificationMode) {
    }





}
