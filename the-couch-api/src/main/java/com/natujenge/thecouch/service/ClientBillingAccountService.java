package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.ClientBillingAccount;
import com.natujenge.thecouch.domain.ClientWallet;
import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.repository.ClientBillingAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientBillingAccountService {

    @Autowired
    WalletService walletService;

    @Autowired
    ClientBillingAccountRepository clientBillingAccountRepository;

    // create new wallet
    public void createBillingAccount(ClientBillingAccount clientBillingAccount) {
        clientBillingAccountRepository.save(clientBillingAccount);
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


    public void updateBillingAccount(float amountBilled,Coach coach, Client client) {
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

        }else{
            clientBillingAccount.setAmountBilled(amountBilled);
        }

        clientBillingAccountRepository.save(clientBillingAccount);
    }

}
