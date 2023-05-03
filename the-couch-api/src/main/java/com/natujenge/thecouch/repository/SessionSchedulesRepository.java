package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.SessionSchedules;
import com.natujenge.thecouch.domain.User;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionSchedulesRepository extends PagingAndSortingRepository<SessionSchedules, Long>, QuerydslPredicateExecutor<SessionSchedules> {
    List<SessionSchedules> findAll();
    List<SessionSchedules> findAllByCoachId(Long id);
    boolean existsByIdAndCoachId(Long id, Long coachId);
    List<SessionSchedules> findAllByCoachIdAndBooked(User user, Boolean status);
}
