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
    OrganizationWalletService  organizationWalletService;
    @Autowired
    CoachWalletService coachWalletService;
    public void createBillingAccount(CoachBillingAccount coachBillingAccount) {

        coachBillingAccountRepository.save(coachBillingAccount);
    }
    public void updateOrganizationAndCoachBillingAccount(float amountBilled, Organization organization, Coach coach) {
        // CHECK wallet balance
        // Update contract payment status and amountDue
        // Get wallet balance should check the last record on dB by client

        OrganizationWallet organizationWallet = organizationWalletService.getWalletRecentRecord(organization.getId());

        CoachWallet coachWallet =  coachWalletService.getCoachWalletRecentRecord(organization.getId(),
                coach.getId());


        float coachWalletBalance = (coachWallet.getWalletBalance() != null)?coachWallet.getWalletBalance():
                0f;

        float organizationWalletBalance = (organizationWallet.getWalletBalance() != null) ? organizationWallet.getWalletBalance() : 0f;
        // retrieve recent billing account record

        float paymentBalance;
        if (coachWalletBalance >= amountBilled) {
            paymentBalance = coachWalletBalance - amountBilled;
            coachWalletService.updateWalletBalance(coachWallet, paymentBalance, organization.getFullName());
        } else {
            paymentBalance = amountBilled - coachWalletBalance;
            coachWalletService.updateWalletBalance(coachWallet, 0f, organization.getFullName());
        }
        float organizationWalletNewBalance = organizationWalletBalance + amountBilled;
        organizationWalletService.updateWalletBalance(organizationWallet, organizationWalletNewBalance);

        // Create new client billing account record
        CoachBillingAccount coachBillingAccount = new CoachBillingAccount();
        coachBillingAccount.setCreatedBy(organization.getFullName());
        coachBillingAccount.setOrganization(organization);
        coachBillingAccount.setCoach(coach);
        coachBillingAccount.setAmountBilled(amountBilled);

        // Save client billing account record
        coachBillingAccountRepository.save(coachBillingAccount);
    }
}


