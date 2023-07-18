package com.natujenge.thecouch.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name="tbl_attachments")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name="attachment_no")
    private String attachmentNumber;


//    @Lob
//    private List<byte[]> files;

    @ElementCollection
    private List<String> links;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    @Column(name = "last_updated_by")
    private String lastUpdatedBy;


    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;


}
