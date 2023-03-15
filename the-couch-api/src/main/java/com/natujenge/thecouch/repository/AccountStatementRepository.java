package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.AccountStatement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AccountStatementRepository extends PagingAndSortingRepository<AccountStatement, Long> {
    Page<AccountStatement> findAllByOrganization_id(Long organizationId, Pageable pageable);

    Page<AccountStatement> findAllByCoach_id(Long coachId, Pageable pageable);

    Page<AccountStatement> findAllByClient_id(Long clientId, Pageable pageable);

    Page<AccountStatement> findAllByCoach_idAndClient_id(Long coachId, Long clientId, Pageable pageable);

    Page<AccountStatement> findAllByOrganization_idAndClient_id(Long organizationId, Long clientId, Pageable pageable);

    Page<AccountStatement> findAllByOrganization_idAndCreatedAtBetween(Long organizationId, LocalDateTime minusMonths, LocalDateTime now, Pageable pageable);

    Page<AccountStatement> findAllByCoach_idAndCreatedAtBetween(Long coachId, LocalDateTime minusMonths, LocalDateTime now, Pageable pageable);

    Page<AccountStatement> findAllByClient_idAndCreatedAtBetween(Long clientId, LocalDateTime minusDays, LocalDateTime now, Pageable pageable);

    Page<AccountStatement> findAllByCoach_idAndClient_idAndCreatedAtBetween(Long coachId, Long clientId, LocalDateTime minusDays, LocalDateTime now, Pageable pageable);

    Page<AccountStatement> findAllByOrganization_idAndClient_idAndCreatedAtBetween(Long organizationId, Long clientId, LocalDateTime minusWeeks, LocalDateTime now, Pageable pageable);
}
