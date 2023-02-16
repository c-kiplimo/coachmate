package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.ClientBillingAccount;
import com.natujenge.thecouch.domain.ClientWallet;
import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.repository.ClientBillingAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ClientBillingAccountService {

    @Autowired
    WalletService walletService;

    @Autowired
    ClientBillingAccountRepository clientBillingAccountRepository;

    // create new wallet
    public void createBillingAccount(ClientBillingAccount clientBillingAccount) {
        clientBillingAccountRepository.save(clientBillingAccount);
    }


    public void updateBillingAccount(float amountBilled,Coach coach, Client client) {
        // CHECK wallet balance
        // Update contract payment status and amountDue
        // Get wallet balance should check the last record on dB by client
        log.info("Amount Due:{} ",amountBilled);

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

}
