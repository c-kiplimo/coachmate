package com.natujenge.thecouch.web.rest.request;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ChangeStatusRequest {
    public String narration;
    public LocalDateTime date;
    public boolean isSendNotification;
}
