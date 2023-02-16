package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.repository.ClientBillingAccountRepository;
import com.natujenge.thecouch.repository.ClientRepository;
import com.natujenge.thecouch.repository.ClientWalletRepository;
import com.natujenge.thecouch.web.rest.dto.ClientWalletDto;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.request.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Slf4j
@Service
public class WalletService {

    @Autowired
    ClientWalletRepository clientWalletRepository;
    @Autowired
    ClientBillingAccountRepository clientBillingAccountRepository;
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ModelMapper modelMapper;

    // Obtain the recent wallet account for the given client
    public ClientWallet getClientWalletRecentRecord(long coachId, long clientId) {

        // obtain latest payment Record
        Optional<ClientWallet> optionalClientWallet = clientWalletRepository.
                findFirstByCoachIdAndClientIdOrderByIdDesc(
                        coachId, clientId);

        if (optionalClientWallet.isEmpty()) {
            throw new IllegalArgumentException("Specified wallet does not exist!!!");
        }

        return optionalClientWallet.get();
    }


    // updates the wallet entry with the provided wallet balance
    public void updateWalletBalance(ClientWallet clientWallet, float paymentBalance, String coachName) {
        clientWallet.setWalletBalance(paymentBalance);
        clientWallet.setLastUpdatedBy(coachName);
        clientWalletRepository.save(clientWallet);
        log.info("client wallet updated!");

    }

    public float updateBillingAccountOnPayment(Coach coach, Client client, float clientBalance) {

        // obtain recent record on billing account

        Optional<ClientBillingAccount> optionalClientBillingAccount = clientBillingAccountRepository.
                findFirstByCoachIdAndClientIdOrderByIdDesc(coach.getId(), client.getId());

        if(optionalClientBillingAccount.isEmpty()){
            throw new IllegalArgumentException("Client Billing Account not found!");
        }

        ClientBillingAccount clientBillingAccountPrevious = optionalClientBillingAccount.get();
        float amountBilled = (clientBillingAccountPrevious.getAmountBilled() != null)?
                clientBillingAccountPrevious.getAmountBilled():
                0f;

        // new client Billing Account record
        ClientBillingAccount clientBillingAccount = new ClientBillingAccount();
        clientBillingAccount.setCreatedBy(coach.getFullName());
        clientBillingAccount.setCoach(coach);
        clientBillingAccount.setClient(client);


        // Calculate Balances
        float paymentBalance;
        float walletBalance = 0f;
        if (clientBalance >= amountBilled){
            paymentBalance = clientBalance - amountBilled;
            walletBalance = paymentBalance;
            clientBillingAccount.setAmountBilled(0f);


        } else if (clientBalance < amountBilled && clientBalance > 0f) {
            paymentBalance = amountBilled-clientBalance;
            walletBalance = 0f;
            clientBillingAccount.setAmountBilled(paymentBalance);
        }
        clientBillingAccountRepository.save(clientBillingAccount);
        return walletBalance ;
    }

    public ClientWallet createPayment(PaymentRequest paymentRequest, Coach coach) {

        // Obtain Client associated with wallet
        Optional<Client> clientOptional = clientRepository.findByIdAndCoachId(paymentRequest.getClientId()
                ,coach.getId());
        if(clientOptional.isEmpty()){
            throw new IllegalArgumentException("Client not found!");
        }
        Client client = clientOptional.get();


        // obtain previous payment Record
        Optional<ClientWallet> previousWalletRecord = Optional.ofNullable(getClientWalletRecentRecord(paymentRequest.getCoachId(), paymentRequest.getClientId()));
        float prevWalletBalance;
        if(previousWalletRecord.get().getWalletBalance().equals(null)){
             prevWalletBalance = 0;
        }else {
             prevWalletBalance = previousWalletRecord.get().getWalletBalance();
        }

        ClientWallet clientWallet = new ClientWallet();
        clientWallet.setAmountDeposited(paymentRequest.getAmount());

        clientWallet.setPaymentCurrency(paymentRequest.getPaymentCurrency());
        clientWallet.setModeOfPayment(paymentRequest.getModeOfPayment());
        clientWallet.setExtPaymentRef(paymentRequest.getExtPaymentRef());


        clientWallet.setClient(client);
        clientWallet.setCoach(coach);

        // mngmnt
        clientWallet.setCreatedBy(coach.getFullName());
        //clientWallet.setOrganization();

        // Update Payment Details based on balance on clientWallet;
        float walletBalance = prevWalletBalance + paymentRequest.getAmount();


        // update billing account
        // return walletBalance
        Float walletBalanceAfter = updateBillingAccountOnPayment(
                coach,client,walletBalance);


        clientWallet.setWalletBalance(walletBalanceAfter);
        clientWallet.setDescription("New payment entry");

        // save wallet
        ClientWallet clientWallet1 = clientWalletRepository.save(clientWallet);
        log.info("Wallet successfully saved");

        return clientWallet1;
        // Map to Dto and return
        //return modelMapper.map(clientWallet1, ClientWalletDto.class);

    }

    // Get all payments by organization Id
    public ListResponse getPaymentsByOrganizationId(int page, int perPage, Long organizationId) {
        log.info("Get all Payments by Organization id {}", organizationId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDto> walletPage;

        // search payments by coach id
        walletPage = clientWalletRepository.findAllByOrganization_id(organizationId, pageable);

        return new ListResponse(walletPage.getContent(),
                walletPage.getTotalPages(), walletPage.getNumberOfElements(),
                walletPage.getTotalElements());
    }

    // Get all payments by coach Id
    public ListResponse getPaymentsByCoachId(int page, int perPage, Long coachId) {
        log.info("Get all Payments by Coach id {}", coachId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDto> walletPage;

        // search payments by coach id
        walletPage = clientWalletRepository.findAllByCoach_id(coachId, pageable);

        return new ListResponse(walletPage.getContent(),
                walletPage.getTotalPages(), walletPage.getNumberOfElements(),
                walletPage.getTotalElements());
    }

    // Get all payments by organization Id
    public ListResponse getPaymentsByClientId(int page, int perPage, Long clientId) {
        log.info("Get all Payments by Client id {}", clientId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDto> walletPage;

        // search payments by coach id
        walletPage = clientWalletRepository.findAllByClient_id(clientId, pageable);

        return new ListResponse(walletPage.getContent(),
                walletPage.getTotalPages(), walletPage.getNumberOfElements(),
                walletPage.getTotalElements());
    }

    public ListResponse getPaymentsByOrganizationIdAndClientId(int page, int perPage, Long organizationId,Long clientId) {
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDto> paymentPage =  clientWalletRepository.findByOrganizationIdAndClientId(
                organizationId,
                clientId,
                pageable
        );
        return new ListResponse(paymentPage.getContent(), paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                paymentPage.getTotalElements());
    }

    public ListResponse getPaymentsByCoachIdIdAndClientId(int page, int perPage, Long coachId,Long clientId) {
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientWalletDto> paymentPage =  clientWalletRepository.findByOrganizationIdAndClientId(
                coachId,
                clientId,
                pageable
        );
        return new ListResponse(paymentPage.getContent(), paymentPage.getTotalPages(), paymentPage.getNumberOfElements(),
                paymentPage.getTotalElements());
    }
}
