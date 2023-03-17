package com.natujenge.thecouch.web.rest.request;

import com.natujenge.thecouch.domain.enums.ClientStatus;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ChangeStatusRequest {
    public String narration;
    public boolean isSendNotification;
}
