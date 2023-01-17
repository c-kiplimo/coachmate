package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.CoachEducation;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
        import org.springframework.data.repository.PagingAndSortingRepository;

        import java.util.List;
        import java.util.Optional;

public interface CoachEducationRepository extends PagingAndSortingRepository<CoachEducation, Long>, QuerydslPredicateExecutor<CoachEducation> {

    List<CoachEducation> findAll();

    void deleteCoachEducationById(long id);

    Optional<CoachEducation> findCoachEducationById(Long id);

    Optional<CoachEducation> findCoachEducationByIdAndCoachID(Long id, Long coachID);
}
