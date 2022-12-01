package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.CoachingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientCoachingScheduleRepository extends JpaRepository<CoachingSchedule,Long> {

}
