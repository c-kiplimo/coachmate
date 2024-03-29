package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.OrganizationWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationWalletRepository extends JpaRepository<OrganizationWallet,Long> {
    Optional<OrganizationWallet> findFirstByOrganizationIdOrderByIdDesc(long organizationId);
}
