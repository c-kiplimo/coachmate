package com.natujenge.thecouch.domain;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_attachments")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String link;


    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime lastUpdatedAt;

    private String lastUpdatedBy;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;


}
