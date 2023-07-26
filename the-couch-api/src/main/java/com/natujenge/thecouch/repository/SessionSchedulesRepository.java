package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.DaysOfTheWeek;
import com.natujenge.thecouch.domain.SessionSchedules;
import com.natujenge.thecouch.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionSchedulesRepository extends JpaRepository<SessionSchedules, Long>, QuerydslPredicateExecutor<SessionSchedules> {
    List<SessionSchedules> findAllByCoach(Optional<User> optionalCoach);

    List<SessionSchedules> findByDayOfTheWeekAndCoach(DaysOfTheWeek day, User user);
}

