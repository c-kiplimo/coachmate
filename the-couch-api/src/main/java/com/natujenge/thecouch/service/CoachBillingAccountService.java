package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.repository.CoachBillingAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CoachBillingAccountService {

    private final CoachBillingAccountRepository coachBillingAccountRepository;


    private final OrganizationWalletService  organizationWalletService;

   private final  CoachWalletService coachWalletService;

    public CoachBillingAccountService(CoachBillingAccountRepository coachBillingAccountRepository, OrganizationWalletService organizationWalletService, CoachWalletService coachWalletService) {
        this.coachBillingAccountRepository = coachBillingAccountRepository;
        this.organizationWalletService = organizationWalletService;
        this.coachWalletService = coachWalletService;
    }

    public void createBillingAccount(CoachBillingAccount coachBillingAccount) {

        coachBillingAccountRepository.save(coachBillingAccount);
    }
    public void updateOrganizationAndCoachBillingAccount(float amountBilled, Organization organization, User coach) {
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
            coachWalletService.updateWalletBalance(coachWallet, paymentBalance, organization.getOrgName());
        } else {
            paymentBalance = amountBilled - coachWalletBalance;
            coachWalletService.updateWalletBalance(coachWallet, 0f, organization.getOrgName());
        }
        float organizationWalletNewBalance = organizationWalletBalance + amountBilled;
        organizationWalletService.updateWalletBalance(organizationWallet, organizationWalletNewBalance);

        // Create new client billing account record
        CoachBillingAccount coachBillingAccount = new CoachBillingAccount();
        coachBillingAccount.setCreatedBy(organization.getOrgName());
        coachBillingAccount.setOrganization(organization);
        coachBillingAccount.setCoach(coach);
        coachBillingAccount.setAmountBilled(amountBilled);

        // Save client billing account record
        coachBillingAccountRepository.save(coachBillingAccount);
    }
}


