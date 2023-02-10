package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.ClientWallet;
import com.natujenge.thecouch.web.rest.dto.ClientWalletDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientWalletRepository extends JpaRepository<ClientWallet,Long> {

    Optional<ClientWallet> findFirstByCoachIdAndClientIdOrderByIdDesc(long coachId, long clientId);

    Page<ClientWalletDto> findAllByOrganization_id(Long organizationId, Pageable pageable);

    Page<ClientWalletDto> findAllByCoach_id(Long coachId, Pageable pageable);

    Page<ClientWalletDto> findAllByClient_id(Long clientId, Pageable pageable);

    Page<ClientWalletDto> findByOrganizationIdAndClientId(Long organizationId, Long clientId, Pageable pageable);


}
