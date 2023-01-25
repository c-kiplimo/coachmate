package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Organization;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends PagingAndSortingRepository<Organization, Long>, QuerydslPredicateExecutor<Organization> {


    Optional<Organization> findBySuperCoachIdAndId(Long superCoachId, Long id);

    boolean existsBySuperCoachIdAndId(Long superCoachId, Long id);

}
