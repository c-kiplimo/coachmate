package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.repository.CoachBillingAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CoachBillingAccountService {
    @Autowired
    CoachBillingAccountRepository coachBillingAccountRepository;
    @Autowired
    CoachWalletService coachWalletService;
    public void createBillingAccount(CoachBillingAccount coachBillingAccount) {

        coachBillingAccountRepository.save(coachBillingAccount);
    }
    public void updateOrganizationAndCoachBillingAccount(float amountBilled, Organization organization, Coach coach) {
        // CHECK wallet balance
        // Update contract payment status and amountDue
        // Get wallet balance should check the last record on dB by client



        CoachWallet coachWallet =  coachWalletService.getCoachWalletRecentRecord(organization.getId(),
                coach.getId());


        float walletBalance = (coachWallet.getWalletBalance() != null)?coachWallet.getWalletBalance():
                0f;
        // retrieve recent billing account record

        // new client Billing Account record
        CoachBillingAccount coachBillingAccount = new CoachBillingAccount();
        coachBillingAccount.setCreatedBy(organization.getFullName());
        coachBillingAccount.setOrganization(organization);
        coachBillingAccount.setCoach(coach);

        float paymentBalance;
        if (walletBalance >= amountBilled){
            paymentBalance = walletBalance - amountBilled;
            coachWalletService.updateWalletBalance(coachWallet, paymentBalance,organization.getFullName());
            coachBillingAccount.setAmountBilled(0f);

        } else if (walletBalance < amountBilled && walletBalance > 0f) {
            paymentBalance = amountBilled-walletBalance;
            coachWalletService.updateWalletBalance(coachWallet, 0f,organization.getFullName());
            coachBillingAccount.setAmountBilled(paymentBalance);
            log.info("amount billed:{}",coachBillingAccount.getAmountBilled());

        }else{
            coachBillingAccount.setAmountBilled(amountBilled);
        }
        log.info("amount billed:{}",coachBillingAccount.getAmountBilled());
        coachBillingAccountRepository.save(coachBillingAccount);
    }
}
