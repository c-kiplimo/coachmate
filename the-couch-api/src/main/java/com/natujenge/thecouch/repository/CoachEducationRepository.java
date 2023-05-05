package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.CoachEducation;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CoachEducationRepository extends PagingAndSortingRepository<CoachEducation, Long>, QuerydslPredicateExecutor<CoachEducation> {

    List<CoachEducation> findAll();

    void deleteCoachEducationById(long id);

    Optional<CoachEducation> findCoachEducationById(Long id);

    List<CoachEducation> findAllByCoachId(long coachId);
    Optional<CoachEducation> findCoachEducationByIdAndCoachId(Long id, Long coachId);
}
