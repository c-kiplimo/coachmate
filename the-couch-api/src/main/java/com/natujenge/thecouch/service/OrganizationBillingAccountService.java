package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.OrganizationBillingAccount;
import com.natujenge.thecouch.repository.OrganizationBillingAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationBillingAccountService {
    @Autowired
    OrganizationBillingAccountRepository organizationBillingAccountRepository;
    public void createBillingAccount(OrganizationBillingAccount organizationBillingAccount) {

        organizationBillingAccountRepository.save(organizationBillingAccount);
    }
}
