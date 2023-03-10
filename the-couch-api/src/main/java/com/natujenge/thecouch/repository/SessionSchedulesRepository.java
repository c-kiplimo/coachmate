package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.SessionSchedules;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionSchedulesRepository extends PagingAndSortingRepository<SessionSchedules, Long>, QuerydslPredicateExecutor<SessionSchedules> {
    List<SessionSchedules> findAll();

    Optional<SessionSchedules> findAllByCoachId(Long id);

    List<SessionSchedules> findAllBySessionDate(LocalDate date);
}
