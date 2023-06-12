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
    private String  FileName;
    private String  attachmentNumber;
    private String  FileType;
    private Long  FileSize;
    private  byte[]  FileContent;


    private String link;
    private String  linkUrl;
    private byte[]  file;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private String createdBy;
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;

    @ManyToOne
    @JoinColumn(name = "session_id")
    Session session;



}
