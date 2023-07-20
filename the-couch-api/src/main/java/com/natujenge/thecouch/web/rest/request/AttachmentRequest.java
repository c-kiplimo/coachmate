package com.natujenge.thecouch.web.rest.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.util.List;

@Data
public class AttachmentRequest {
    private String link;
}
