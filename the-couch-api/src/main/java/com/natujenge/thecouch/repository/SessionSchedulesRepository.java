package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.SessionSchedules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionSchedulesRepository extends JpaRepository<SessionSchedules, Long>, QuerydslPredicateExecutor<SessionSchedules> {
}
