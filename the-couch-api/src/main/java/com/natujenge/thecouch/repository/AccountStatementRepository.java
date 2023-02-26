package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.AccountStatement;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountStatementRepository extends PagingAndSortingRepository<AccountStatement, Long> {
    Page<AccountStatement> findAllByOrganization_id(Long organizationId, Pageable pageable);

    Page<AccountStatement> findAllByCoach_id(Long coachId, Pageable pageable);

    Page<AccountStatement> findAllByClient_id(Long clientId, Pageable pageable);

    Page<AccountStatement> findAllByCoach_idAndClient_id(Long coachId, Long clientId, Pageable pageable);

    Page<AccountStatement> findAllByOrganization_idAndClient_id(Long organizationId, Long clientId, Pageable pageable);
}
