package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.domain.Contract;
import com.natujenge.thecouch.domain.Notification;
import com.natujenge.thecouch.repository.NotificationRepository;
import com.natujenge.thecouch.web.rest.request.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification getNotificationById(long id) {
        log.info("Get Notification of id {}",id);
        Optional<Notification> notification = notificationRepository.findById(id);

        if (notification.isPresent()) {
            return notification.get();
        } else {
            throw new IllegalStateException("Notification with " + id + " not found ");
        }
    }
    public void createNotificationOnContractCreation(NotificationRequest notificationRequest, Contract contract, Coach coach) {
        log.info("Creating new notification");
        Notification notification = new Notification();
        notification.setNotificationMode(notificationRequest.getNotificationMode());
        notification.setSubject(notificationRequest.getSubject());
        notification.setSrcAddress(notificationRequest.getSrcAddress());
        notification.setDstAddress(notificationRequest.getDstAddress());
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


}
