package com.natujenge.thecouch.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;
    @Entity
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "tbl_responses")
    public class Response {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private String email;
        private String message;

        @CreationTimestamp
        @Column(nullable = false)
        private LocalDateTime createdAt;
    }


