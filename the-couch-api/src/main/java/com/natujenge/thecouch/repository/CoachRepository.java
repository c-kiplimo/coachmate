package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoachRepository extends PagingAndSortingRepository<Coach,Long>, QuerydslPredicateExecutor<Coach> {
    Optional<Coach> getCoachById(Long id);
    List<Coach> findAll();
    void deleteCoachById(long id);
    Optional<Coach> findCoachById(Long id);
    
    List<Coach> findByOrganizationId(Long organizationId);

}
