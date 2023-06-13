package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Notification;
import com.natujenge.thecouch.web.rest.dto.NotificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<NotificationDTO> findBySessionId(Long sessionId, Pageable pageable);

    Page<NotificationDTO> findByClientIdAndCoachId(Long coachId, Long clientId, Pageable pageable);

    Page<NotificationDTO> findByClientId(Long clientId, Pageable pageable);
}
