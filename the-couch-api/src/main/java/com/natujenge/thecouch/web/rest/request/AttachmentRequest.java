package com.natujenge.thecouch.web.rest.request;

import lombok.Data;

@Data
public class AttachmentRequest {
    private String fileName;
    private String fileType;
    private String fileDownloadUri;
    private Long size;
    private String createdBy;
    private String attachmentNumber;
}
