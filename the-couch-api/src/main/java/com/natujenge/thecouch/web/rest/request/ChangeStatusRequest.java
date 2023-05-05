package com.natujenge.thecouch.web.rest.request;

import lombok.Data;
@Data
public class ChangeStatusRequest {
    public String narration;
    public boolean isSendNotification;
}
