package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.CoachWallet;
import com.natujenge.thecouch.repository.CoachWalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CoachWalletService {
    @Autowired
    CoachWalletRepository coachWalletRepository;
    public CoachWallet getCoachWalletRecentRecord(long organizationId, long coachId) {
        log.info("Get coach wallet recent record for organization id {} and coach id {}", organizationId, coachId);
        // obtain latest payment Record
        Optional<CoachWallet> optionalCoachWallet = coachWalletRepository.
                findFirstByOrganizationIdAndUserIdOrderByIdDesc(
                        organizationId, coachId);

        if (optionalCoachWallet.isEmpty()) {
            throw new IllegalArgumentException("Specified wallet does not exist!!!");

        }

        return optionalCoachWallet.get();
    }
    public CoachWallet getWalletRecentRecord( long coachId) {
        log.info("Get coach wallet recent record for organization id {} and coach id {}", coachId);
        // obtain latest payment Record
        Optional<CoachWallet> optionalCoachWallet = coachWalletRepository.
                findFirstByUserIdOrderByIdDesc(coachId);

        if (optionalCoachWallet.isEmpty()) {
            throw new IllegalArgumentException("Specified wallet does not exist!!!");

        }

        return optionalCoachWallet.get();
    }
    public void updateWalletBalance(CoachWallet coachWallet, Float paymentBalance, String orgName) {
        Float previousBalance = coachWallet.getWalletBalance();
        coachWallet.setWalletBalanceBefore(previousBalance);
        coachWallet.setWalletBalance(paymentBalance);
        coachWallet.setLastUpdatedBy(orgName);
        coachWalletRepository.save(coachWallet);
        log.info("client wallet updated!");
    }
}
