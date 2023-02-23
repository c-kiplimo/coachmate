package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.ClientBillingAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientBillingAccountRepository extends PagingAndSortingRepository<ClientBillingAccount,Long>,
        QuerydslPredicateExecutor<ClientBillingAccount> {

    Optional<ClientBillingAccount> findFirstByCoachIdAndClientIdOrderByIdDesc(Long coachId,Long clientId);


    Page<ClientBillingAccount> findAllByCoachId(Long coachId, Pageable pageable);

    Page<ClientBillingAccount> findAllByCoach_organization_id(Long organizationId, Pageable pageable);

    Page<ClientBillingAccount> findAllByClient_id(Long clientId, Pageable pageable);
}
