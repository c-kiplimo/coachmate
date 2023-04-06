package com.natujenge.thecouch.domain;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;

@Table(name = "tbl_sessionResources")
@Data
@Entity
public class Attachments {

    @Column(name = "filename", length = 50)
    private String filename;
    @Column(name = "original_name", length = 100)
    private String originalName;
    @Column(name = "mime_type", length = 100)
    private String mimeType;
    //    @Lob
//    private byte[] data;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private String createdBy;
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;
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



    @ManyToOne
    @JoinColumn(name = "session_id")
    Session session;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    Coach coach;

    @ManyToOne
    @JoinColumn(name = "client_id")
    Client client;

    @ManyToOne
    @JoinColumn(name = "org_id_id")
    Organization organization;
    @Override public String toString() {
        return "Attachments{" +
                "id=" + id +
                ", FileName='" + FileName + '\'' +
                ", attachmentNumber='" + attachmentNumber + '\'' +
                ", FileType='" + FileType + '\'' +
                ", FileSize=" + FileSize +
                ", FileContent=" + Arrays.toString(FileContent) +
                ", link='" + link + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", file=" + Arrays.toString(file) +
                ", session=" + session +
                ", coach=" + coach +
                ", client=" + client +
                ", organization=" + organization +
                '}';
    }

}
