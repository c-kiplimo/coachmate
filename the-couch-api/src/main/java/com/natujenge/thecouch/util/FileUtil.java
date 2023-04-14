package com.natujenge.thecouch.util;

import com.natujenge.thecouch.config.Constants;
import com.natujenge.thecouch.service.dto.UploadResponse;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

public class FileUtil {

    public static UploadResponse uploadFile(MultipartFile file, Logger log) {
        UploadResponse uploadResponse = new UploadResponse();

        if (!Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {

            try {
                String filename = UUID.randomUUID().toString();

                BufferedOutputStream outputStream = new BufferedOutputStream(
                        new FileOutputStream(
                                new File(Constants.UPLOADS_FOLDER, filename)
                        )
                );

                outputStream.write(file.getBytes());
                outputStream.flush();
                outputStream.close();
                uploadResponse.setFilename(filename);
                uploadResponse.setMimeType(file.getContentType());
                uploadResponse.setOriginalName(file.getOriginalFilename());
                uploadResponse.setCode(HttpStatus.OK.value());
                uploadResponse.setStatus("SUCCESS");
                uploadResponse.setStatusReason("Upload success");

            } catch (IOException ex) {
                log.error("Error uploading file: ", ex);
                uploadResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                uploadResponse.setStatus("FAILED");
                uploadResponse.setStatusReason(ex.getMessage());
            }

        } else {
            uploadResponse.setCode(HttpStatus.BAD_REQUEST.value());
            uploadResponse.setStatus("FAILED");
            uploadResponse.setStatusReason("Invalid File Uploaded");
        }

        return uploadResponse;
    }

    public static byte[] getImage(String filename, Logger logger){
        Path path = Paths.get(Constants.UPLOADS_FOLDER+"/"+filename);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            logger.info("Error while loading file: {}", e.getMessage());
            //throw new RuntimeException(e);
        }
        return null;
    }

    public static String getImageUrl(String filename){

        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/uploads/"+filename).toUriString();
    }
}
