package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.User;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoachRepository extends PagingAndSortingRepository<User,Long>, QuerydslPredicateExecutor<User> {
    Optional<User> getCoachById(Long id);
    List<User> findAll();
    void deleteCoachById(long id);
    Optional<User> findCoachById(Long id);

    List<User> findByOrganizationId(Long organizationId);

    List<User> findAllByOrganizationId(Long organizationId);

}
