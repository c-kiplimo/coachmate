package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.repository.ClientWalletRepository;
import com.natujenge.thecouch.web.rest.dto.ClientWalletDto;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.dto.PaymentDto;
import com.natujenge.thecouch.web.rest.request.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class WalletService {

    @Autowired
    ClientWalletRepository clientWalletRepository;

    @Autowired
    ClientService clientService;
    @Autowired
    ContractService contractService;
    @Autowired
    ClientBillingAccountService clientBillingAccountService;

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

    public void createWallet(ClientWallet clientWallet) {
        log.info("Creating client wallet for client of id {}", clientWallet.getClient().getId());
        clientWalletRepository.save(clientWallet);
        log.info("Wallet created");


    }

    public ClientWalletDto createPayment(PaymentRequest paymentRequest, Coach coach) {

        // Obtain Client associated with wallet
        Client client = clientService.getClient(paymentRequest.getClientId(), coach.getId());

        // obtain previous payment Record
        ClientWallet previousWalletRecord = getClientWalletRecentRecord(coach.getId(), paymentRequest.getClientId());
        float prevWalletBalance = previousWalletRecord.getWalletBalance();

        ClientWallet clientWallet = new ClientWallet();
        clientWallet.setAmountDeposited(paymentRequest.getAmount());

        clientWallet.setPaymentCurrency(paymentRequest.getPaymentCurrency());
        clientWallet.setModeOfPayment(paymentRequest.getModeOfPayment());
        clientWallet.setExtPaymentRef(paymentRequest.getExtPaymentRef());


        clientWallet.setClient(client);
        clientWallet.setCoach(coach);

        // mngmnt
        clientWallet.setCreatedBy(coach.getFullName());

        // Update Payment Details based on balance on clientWallet;
        float walletBalance = prevWalletBalance + paymentRequest.getAmount();


        // update billing account
        // return walletBalance
        Float walletBalanceAfter = clientBillingAccountService.updateBillingAccountOnPayment(
                coach,client,walletBalance);


        clientWallet.setWalletBalance(walletBalanceAfter);
        clientWallet.setDescription("New payment entry");

        // save wallet
        ClientWallet clientWallet1 = clientWalletRepository.save(clientWallet);
        log.info("Wallet successfully saved");

        // Map to Dto and return
        return modelMapper.map(clientWallet1, ClientWalletDto.class);

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
