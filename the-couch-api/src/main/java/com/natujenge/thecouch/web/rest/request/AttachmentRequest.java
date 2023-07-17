package com.natujenge.thecouch.web.rest.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.util.List;

@Data
public class AttachmentRequest {
    private List<byte[]> files; // Allow multiple files to be uploaded
    private List<String> links;
    private Long sessionId; // This will be used to set the 'Session' relationship

    // You can also include any necessary constructors, getters, and setters here.
}
