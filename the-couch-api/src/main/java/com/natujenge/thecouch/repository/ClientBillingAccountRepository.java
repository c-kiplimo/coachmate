package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.ClientBillingAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientBillingAccountRepository extends JpaRepository<ClientBillingAccount,Long> {

    Optional<ClientBillingAccount> findFirstByCoachIdAndClientIdOrderByIdDesc(Long coachId,Long clientId);


    Page<ClientBillingAccount> findAllByCoach_id(Long coachId, Pageable pageable);
}
