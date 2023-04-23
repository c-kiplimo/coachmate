package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.CoachWallet;
import com.natujenge.thecouch.domain.OrganizationWallet;
import com.natujenge.thecouch.repository.OrganizationWalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class OrganizationWalletService {
    @Autowired
    OrganizationWalletRepository organizationWalletRepository;
    public OrganizationWallet getWalletRecentRecord( long organizationId) {
        log.info("Get organization wallet recent record for organization id {} ", organizationId);
        // obtain latest payment Record
        Optional<OrganizationWallet> optionalOrganizationWallet = organizationWalletRepository.
                findFirstByOrganizationIdOrderByIdDesc(organizationId);

        if (optionalOrganizationWallet.isEmpty()) {
            throw new IllegalArgumentException("Specified wallet does not exist!!!");

        }

        return optionalOrganizationWallet.get();
    }
    public void updateWalletBalance(OrganizationWallet organizationWallet, Float paymentBalance) {
        Float previousBalance = organizationWallet.getWalletBalance();
        organizationWallet.setWalletBalanceBefore(previousBalance);
        organizationWallet.setWalletBalance(paymentBalance);
        organizationWallet.setLastUpdatedBy(organizationWallet.getOrganization().getOrgName());
        organizationWalletRepository.save(organizationWallet);
        log.info("organization wallet updated!");
    }
}
