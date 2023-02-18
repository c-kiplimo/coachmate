package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Notification;
import com.natujenge.thecouch.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public List<Notification> getAllNotifications() {
        log.info("Get all Notifications");
        return notificationRepository.findAll();

    }

    public Notification createNotification(Notification notificationRequest) {
        // TODO: Do some validations then add notification

        return notificationRepository.save(notificationRequest);
    }


}
