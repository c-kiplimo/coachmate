package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.repository.ClientBillingAccountRepository;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class ClientBillingAccountService {

    @Autowired
    ClientWalletService walletService;
    @Autowired
    ClientBillingAccountRepository clientBillingAccountRepository;
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
   
    public void updateBillingAccount(float amountBilled,Coach coach,Client client) {
        // CHECK wallet balance
        // Update contract payment status and amountDue
        // Get wallet balance should check the last record on dB by client

        

        ClientWallet clientWallet =  walletService.getClientWalletRecentRecord(coach.getId(),
                client.getId());


        float walletBalance = (clientWallet.getWalletBalance() != null)?clientWallet.getWalletBalance():
                0f;
        // retrieve recent billing account record

        // new client Billing Account record
        ClientBillingAccount clientBillingAccount = new ClientBillingAccount();
        clientBillingAccount.setCreatedBy(coach.getFullName());
        clientBillingAccount.setCoach(coach);
        clientBillingAccount.setClient(client);

        float paymentBalance;
        if (walletBalance >= amountBilled){
            paymentBalance = walletBalance - amountBilled;
            walletService.updateWalletBalance(clientWallet, paymentBalance,coach.getFullName());
            clientBillingAccount.setAmountBilled(0f);

        } else if (walletBalance < amountBilled && walletBalance > 0f) {
            paymentBalance = amountBilled-walletBalance;
            walletService.updateWalletBalance(clientWallet, 0f,coach.getFullName());
            clientBillingAccount.setAmountBilled(paymentBalance);
            log.info("amount billed:{}",clientBillingAccount.getAmountBilled());

        }else{
            clientBillingAccount.setAmountBilled(amountBilled);
        }
log.info("amount billed:{}",clientBillingAccount.getAmountBilled());
        clientBillingAccountRepository.save(clientBillingAccount);
    }
    public void updateOrganizationAndClientBillingAccount(float amountBilled,Organization organization,Client client) {
        // CHECK wallet balance
        // Update contract payment status and amountDue
        // Get wallet balance should check the last record on dB by client



        ClientWallet clientWallet =  walletService.getClientWalletRecentRecordByOrganization(organization.getId(),
                client.getId());


        float walletBalance = (clientWallet.getWalletBalance() != null)?clientWallet.getWalletBalance():
                0f;
        // retrieve recent billing account record

        // new client Billing Account record
        ClientBillingAccount clientBillingAccount = new ClientBillingAccount();
        clientBillingAccount.setCreatedBy(organization.getFullName());
        clientBillingAccount.setOrganization(organization);
        clientBillingAccount.setClient(client);

        float paymentBalance;
        if (walletBalance >= amountBilled){
            paymentBalance = walletBalance - amountBilled;
            walletService.updateWalletBalance(clientWallet, paymentBalance,organization.getFullName());
            clientBillingAccount.setAmountBilled(0f);

        } else if (walletBalance < amountBilled && walletBalance > 0f) {
            paymentBalance = amountBilled-walletBalance;
            walletService.updateWalletBalance(clientWallet, 0f,organization.getFullName());
            clientBillingAccount.setAmountBilled(paymentBalance);
            log.info("amount billed:{}",clientBillingAccount.getAmountBilled());

        }else{
            clientBillingAccount.setAmountBilled(amountBilled);
        }
        log.info("amount billed:{}",clientBillingAccount.getAmountBilled());
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
