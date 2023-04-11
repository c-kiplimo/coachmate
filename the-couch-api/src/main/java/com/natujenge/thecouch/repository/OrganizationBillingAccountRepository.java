package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.OrganizationBillingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationBillingAccountRepository extends JpaRepository<OrganizationBillingAccount,Long> {
}
