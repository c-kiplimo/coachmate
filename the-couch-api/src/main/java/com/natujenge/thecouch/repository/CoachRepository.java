package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Coach;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoachRepository extends PagingAndSortingRepository<Coach,Long>, QuerydslPredicateExecutor<Coach> {
    Optional<Coach> getCoachById(Long id);

}
