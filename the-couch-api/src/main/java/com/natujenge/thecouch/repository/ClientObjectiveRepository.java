package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.CoachingObjective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientObjectiveRepository extends JpaRepository<CoachingObjective,Long> {

}
