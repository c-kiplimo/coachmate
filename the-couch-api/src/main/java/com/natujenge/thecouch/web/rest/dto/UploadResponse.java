package com.natujenge.thecouch.web.rest.dto;

import lombok.Data;

@Data
public class UploadResponse {
    private String filename;
    private String originalName;
    private String mimeType;
    private String status;
    private String statusReason;
    private int code;
}
