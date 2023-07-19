package com.natujenge.thecouch.repository;


import com.natujenge.thecouch.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,Long> {
    List<Attachment> findBySessionId(Long sessionId);
}
