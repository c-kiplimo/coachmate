package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.ClientWallet;
import com.natujenge.thecouch.domain.CoachWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoachWalletRepository extends JpaRepository<CoachWallet,Long> {
    Optional<CoachWallet> findFirstByOrganizationIdAndCoachIdOrderByIdDesc(long organizationId, long coachId);
}
