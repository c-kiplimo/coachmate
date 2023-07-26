package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.DaysOfTheWeek;
import com.natujenge.thecouch.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DaysOfTheWeekRepository extends JpaRepository<DaysOfTheWeek, Long>, QuerydslPredicateExecutor<DaysOfTheWeek> {

    List<DaysOfTheWeek> findAllByCoach(User coach);

}
