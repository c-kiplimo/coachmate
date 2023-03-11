package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Attachments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentsRepository extends JpaRepository<Attachments,Long> {
    List<Attachments> findBySessionId(Long sessionId);
}
