package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.repository.ClientBillingAccountRepository;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class ClientBillingAccountService {


    private final ClientWalletService walletService;

    private final OrganizationWalletService organizationWalletService;

   private final OrganizationBillingAccountService organizationBillingAccountService;

   private final CoachWalletService coachWalletService;

    private final ClientBillingAccountRepository clientBillingAccountRepository;

    public ClientBillingAccountService(ClientWalletService walletService, OrganizationWalletService organizationWalletService, OrganizationBillingAccountService organizationBillingAccountService, CoachWalletService coachWalletService, ClientBillingAccountRepository clientBillingAccountRepository) {
        this.walletService = walletService;
        this.organizationWalletService = organizationWalletService;
        this.organizationBillingAccountService = organizationBillingAccountService;
        this.coachWalletService = coachWalletService;
        this.clientBillingAccountRepository = clientBillingAccountRepository;
    }

    // Get all billingAccounts by coach Id
    public ListResponse getBillingAccountByCoachId(int page, int perPage, Long coachId) {
        log.info("Get all billing accounts by Coach id {}", coachId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC,    "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientBillingAccount> clientBillingAccountPage;

        // search billingAccount by coach id
        clientBillingAccountPage = clientBillingAccountRepository.findAllByCoachId(coachId, pageable);
        log.info("client billing account page:{}",clientBillingAccountPage.getContent());
        return new ListResponse(clientBillingAccountPage.getContent(),
                clientBillingAccountPage.getTotalPages(),clientBillingAccountPage.getNumberOfElements(),
                clientBillingAccountPage.getTotalElements());
    }

    // create new billing account
    public void createBillingAccount(ClientBillingAccount clientBillingAccount) {

        clientBillingAccountRepository.save(clientBillingAccount);
    }

    public void updateBillingAccount(float amountBilled, User coach, User client) {
        // Retrieve client's most recent wallet record
        ClientWallet clientWallet = walletService.getClientWalletRecentRecord(coach.getId(), client.getId());

        // Retrieve coach's most recent wallet record


        // Get client's wallet balance
        float clientWalletBalance = (clientWallet.getWalletBalance() != null) ? clientWallet.getWalletBalance() : 0f;




        // Determine payment balance and update client's wallet balance
        float paymentBalance;
        if (clientWalletBalance >= amountBilled) {
            paymentBalance = clientWalletBalance - amountBilled;
            walletService.updateWalletBalance(clientWallet, paymentBalance, coach.getFullName());
        } else {
            paymentBalance = amountBilled - clientWalletBalance;
            walletService.updateWalletBalance(clientWallet, 0f, coach.getFullName());
        }

        // Update coach's wallet balance with the amount billed


        // Create new client billing account record
        ClientBillingAccount clientBillingAccount = new ClientBillingAccount();
        clientBillingAccount.setCreatedBy(coach.getFullName());
        clientBillingAccount.setUser(coach);
        clientBillingAccount.setUser(client);
        clientBillingAccount.setAmountBilled(amountBilled);

        // Save client billing account record
        clientBillingAccountRepository.save(clientBillingAccount);
    }

    public void updateOrganizationAndClientBillingAccount(float amountBilled,Organization organization,User client) {
        // CHECK wallet balance
        // Update contract payment status and amountDue
        // Get wallet balance should check the last record on dB by client
       // Retrieve coach's most recent wallet record



        ClientWallet clientWallet =  walletService.getClientWalletRecentRecord(organization.getId(),
                client.getId());


        float clientWalletBalance = (clientWallet.getWalletBalance() != null)?clientWallet.getWalletBalance():
                0f;
         // retrieve recent billing account record

        // new client Billing Account record

        float paymentBalance;
        if (clientWalletBalance >= amountBilled) {
            paymentBalance = clientWalletBalance - amountBilled;
            walletService.updateWalletBalance(clientWallet, paymentBalance, organization.getOrgName());
        } else {
            paymentBalance = amountBilled - clientWalletBalance;
            walletService.updateWalletBalance(clientWallet, 0f, organization.getOrgName());
        }

        // Create new client billing account recordtbl_coach_billing_accountm
        ClientBillingAccount clientBillingAccount = new ClientBillingAccount();
        clientBillingAccount.setCreatedBy(organization.getOrgName());
        clientBillingAccount.setOrganization(organization);
        clientBillingAccount.setUser(client);
        clientBillingAccount.setAmountBilled(amountBilled);

        // Save client billing account record
        clientBillingAccountRepository.save(clientBillingAccount);
    }

    public ListResponse getBillingAccountByOrganizationId(int perPage, int page, Long organizationId) {
        log.info("Get all billing accounts by organization id {}", organizationId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientBillingAccount> clientBillingAccountPage;

        // search billingAccount by org id
        clientBillingAccountPage = clientBillingAccountRepository.findAllByCoach_organization_id(organizationId, pageable);

        return new ListResponse(clientBillingAccountPage.getContent(),
                clientBillingAccountPage.getTotalPages(),clientBillingAccountPage.getNumberOfElements(),
                clientBillingAccountPage.getTotalElements());
    }

    public ListResponse getBillingAccountByClientId(int perPage, int page, Long clientId) {
        log.info("Get all billing accounts by client id {}", clientId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientBillingAccount> clientBillingAccountPage;

        // search billingAccount by client id
        clientBillingAccountPage = clientBillingAccountRepository.findAllByClient_id(clientId, pageable);

        return new ListResponse(clientBillingAccountPage.getContent(),
                clientBillingAccountPage.getTotalPages(),clientBillingAccountPage.getNumberOfElements(),
                clientBillingAccountPage.getTotalElements());
    }


    public ClientBillingAccount getClientBillingAccountByCoachIdAndClientId(Long id, Long id1) {
        return clientBillingAccountRepository.findByCoach_idAndClient_id(id,id1);
    }

}
