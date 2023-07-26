package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.ClientWallet;
import com.natujenge.thecouch.web.rest.dto.ClientWalletDTO;
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

    Page<ClientWalletDTO> findAllByOrganization_id(Long organizationId, Pageable pageable);

    Page<ClientWalletDTO> findAllByCoach_id(Long coachId, Pageable pageable);

    Page<ClientWalletDTO> findAllByClient_id(Long clientId, Pageable pageable);

    Page<ClientWalletDTO> findByOrganizationIdAndClientId(Long organizationId, Long clientId, Pageable pageable);
    Page<ClientWalletDTO> findAllByCoach_idAndCreatedAtBetween(Long coachId, LocalDateTime minusMonths, LocalDateTime now, Pageable pageable);

    Page<ClientWalletDTO> findAllByOrganization_idAndCreatedAtBetween(Long organizationId, LocalDateTime minusDays, LocalDateTime now, Pageable pageable);

    Page<ClientWalletDTO> findAllByClient_idAndCreatedAtBetween(Long clientId, LocalDateTime minusDays, LocalDateTime now, Pageable pageable);
}
