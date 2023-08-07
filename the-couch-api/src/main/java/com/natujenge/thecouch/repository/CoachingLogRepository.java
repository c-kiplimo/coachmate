package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.CoachingLog;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachingLogRepository extends PagingAndSortingRepository<CoachingLog, Long>, QuerydslPredicateExecutor<CoachingLog> {

    Page<CoachingLog> findAll(Example<CoachingLog> example, Pageable pageable);

    void deleteAllByIdInAndCoachId(List<Long> coachingLogIds, Long coachId);

}
