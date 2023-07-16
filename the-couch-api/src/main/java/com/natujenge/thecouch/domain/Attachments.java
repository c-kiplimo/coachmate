package com.natujenge.thecouch.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "tbl_sessionResources")
@Data
@Entity
public class Attachments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "attachment_number")
    private String attachmentNumber;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Lob
    @Column(name = "file_content", columnDefinition = "BLOB")
    private byte[] fileContent;


    @Column(name = "link_url")
    private String linkUrl;

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
