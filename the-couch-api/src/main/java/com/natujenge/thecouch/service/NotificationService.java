package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Contract;
import com.natujenge.thecouch.domain.Notification;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.NotificationRepository;
import com.natujenge.thecouch.repository.SessionRepository;
import com.natujenge.thecouch.service.mapper.SessionMapper;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.dto.NotificationDto;
import com.natujenge.thecouch.web.rest.dto.SessionDTO;
import com.natujenge.thecouch.web.rest.request.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SessionRepository sessionRepository;

    private final SessionMapper sessionMapper;

    public NotificationService(NotificationRepository notificationRepository, SessionRepository sessionRepository, SessionMapper sessionMapper) {
        this.notificationRepository = notificationRepository;
        this.sessionRepository = sessionRepository;
        this.sessionMapper = sessionMapper;
    }

    public Notification getNotificationById(long id, Long coachId) {
        log.info("Get Notification of id {}", id);
        Optional<Notification> notification = notificationRepository.findById(id);

        if (notification.isPresent()) {
            return notification.get();
        } else {
            throw new IllegalStateException("Notification with " + id + " not found ");
        }
    }

    public void createNotificationOnContractCreation(NotificationRequest notificationRequest, Contract contract, User coach) {
        log.info("Creating new notification");
        Notification notification = new Notification();
        notification.setNotificationMode(notificationRequest.getNotificationMode());
        notification.setSubject(notificationRequest.getSubject());
        notification.setSourceAddress(notificationRequest.getSrcAddress());
        notification.setDestinationAddress(notificationRequest.getDstAddress());
        notification.setContent(notificationRequest.getContent());
        notification.setSentAt(LocalDateTime.now());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setCreatedBy(coach.getFullName());
        notification.setContract(contract);
        notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications() {
        log.info("Get all Notifications");
        return notificationRepository.findAll();


    }

    public Notification createNotification(Notification notificationRequest) {
        // TODO: Do some validations then add notification

        return notificationRepository.save(notificationRequest);
    }


    public Object getAllNotifications(int page, int perPage, Long coachId) {
        return notificationRepository.findAll();
    }


    public void createNotificationByCoachId(NotificationRequest notificationRequest, long coachId) {
    }

    // get notification by session and coach id
    public ListResponse filterBySessionId(int page, int perPage, Long sessionId) {
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        // Check if session exists
        Optional<SessionDTO> optionalNotification = sessionRepository.findById(sessionId).map(sessionMapper::toDto);
        if (optionalNotification.isEmpty()) {
            throw new IllegalArgumentException("session not Found");
        }

        Page<NotificationDto> notificationPage = notificationRepository.findBySessionId(
                sessionId,
                pageable
        );

        return new ListResponse(notificationPage.getContent(), notificationPage.getTotalPages(), notificationPage.getNumberOfElements(),
                notificationPage.getTotalElements());
    }

    //get notification by client and coach id
    public ListResponse filterByClientIdAndCoachId(int page, int perPage, Long clientId, Long coachId) {
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<NotificationDto> notificationPage = notificationRepository.findByClientIdAndCoachId(
                coachId,
                clientId,
                pageable
        );
        log.info("Notification Found!");
        return new ListResponse(notificationPage.getContent(), notificationPage.getTotalPages(), notificationPage.getNumberOfElements(),
                notificationPage.getTotalElements());
    }

    public ListResponse filterByClientId(int page, int perPage, Long clientId) {
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<NotificationDto> notificationPage = notificationRepository.findByClientId(
                clientId,
                pageable
        );
        log.info("Notification Found!");
        return new ListResponse(notificationPage.getContent(), notificationPage.getTotalPages(), notificationPage.getNumberOfElements(),
                notificationPage.getTotalElements());
    }
}
