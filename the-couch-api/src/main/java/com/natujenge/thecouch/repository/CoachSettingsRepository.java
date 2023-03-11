package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.CoachSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachSettingsRepository extends JpaRepository<CoachSettings, Long> {

}
