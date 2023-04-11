package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.OrganizationWallet;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationWalletRepository extends JpaRepository<OrganizationWallet,Long> {
}
