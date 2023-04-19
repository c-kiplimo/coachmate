package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.ClientWallet;
import com.natujenge.thecouch.domain.enums.StatementPeriod;
import com.natujenge.thecouch.web.rest.dto.ClientWalletDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
@Repository
public interface ClientWalletRepository extends PagingAndSortingRepository<ClientWallet,Long>,
        QuerydslPredicateExecutor<ClientWallet> {

    Optional<ClientWallet> findFirstByCoachIdAndClientIdOrderByIdDesc(long coachId, long clientId);
    Optional<ClientWallet> findFirstByOrganizationIdAndClientIdOrderByIdDesc(long organizationId, long clientId);

    Page<ClientWalletDto> findAllByOrganization_id(Long organizationId, Pageable pageable);

    Page<ClientWalletDto> findAllByCoach_id(Long coachId, Pageable pageable);

    Page<ClientWalletDto> findAllByClient_id(Long clientId, Pageable pageable);

    Page<ClientWalletDto> findByOrganizationIdAndClientId(Long organizationId, Long clientId, Pageable pageable);
    Page<ClientWalletDto> findAllByCoach_idAndCreatedAtBetween(Long coachId, LocalDateTime minusMonths, LocalDateTime now, Pageable pageable);

    Page<ClientWalletDto> findAllByOrganization_idAndCreatedAtBetween(Long organizationId, LocalDateTime minusDays, LocalDateTime now, Pageable pageable);

    Page<ClientWalletDto> findAllByClient_idAndCreatedAtBetween(Long clientId, LocalDateTime minusDays, LocalDateTime now, Pageable pageable);
}
