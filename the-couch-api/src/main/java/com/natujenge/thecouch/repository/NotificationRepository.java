package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Notification;
import com.natujenge.thecouch.web.rest.dto.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<NotificationDto> findBySessionId(Long sessionId, Pageable pageable);

    Page<NotificationDto> findByClientIdAndCoachId(Long coachId, Long clientId, Pageable pageable);
}
