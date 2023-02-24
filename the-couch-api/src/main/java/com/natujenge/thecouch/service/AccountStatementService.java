package com.natujenge.thecouch.service;
import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.Coach;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.natujenge.thecouch.repository.AccountStatementRepository;
import java.time.LocalDateTime;
import com.natujenge.thecouch.repository.ClientWalletRepository;

@Service
@Slf4j
public class AccountStatementService {
    @Autowired
    private AccountStatementRepository accountStatementRepository;
    @Autowired
    ClientWalletRepository walletRepository;


    // create new account statement
    public void createAccountStatement(AccountStatement accountStatement) {
        // save account statement
        accountStatementRepository.save(accountStatement);
    }
    // update account statement
    public void updateAccountStatement(Coach coach, Client client, float amountIn, float balanceBefore,
                                       float balanceAfter) {
        log.info("Update account statement for client {} by coach {}", client.getId(), coach.getId());

        AccountStatement accountStatement = new AccountStatement();
        accountStatement.setCoach(coach);
        accountStatement.setClient(client);
        accountStatement.setAmountIn(amountIn);
        accountStatement.setBalanceBefore(balanceBefore);
        accountStatement.setBalanceAfter(balanceAfter);
        accountStatement.setLastUpdatedBy("System");
        accountStatement.setLastUpdatedAt(LocalDateTime.now());
        accountStatement.setDescription("Amount deposited");
        // save account statement
        log.info("saving account statement:{}",accountStatement);
        accountStatementRepository.save(accountStatement);
        log.info("account statement saved");













    }

}
