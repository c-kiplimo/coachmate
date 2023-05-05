package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.NotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Long> {

//    Optional<NotificationSettingsDto> findAllByCoachId(Long coachId);

    Optional<NotificationSettings> findByUserId(Long coachId);

}