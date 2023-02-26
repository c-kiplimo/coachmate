package com.natujenge.thecouch.service;
import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.Client;
import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.web.rest.dto.ClientWalletDto;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    // Get account statement by organization Id
    public ListResponse getStatementByOrganizationId(int page, int perPage, Long organizationId) {
        log.info("Get account statement by organization Id{}", organizationId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<AccountStatement> statementPage;
        statementPage = accountStatementRepository.findAllByOrganization_id(organizationId, pageable);

        return new ListResponse(statementPage.getContent(),
                statementPage.getTotalPages(), statementPage.getNumberOfElements(),
                statementPage.getTotalElements());
    }
    // Get account statement by coach Id
    public ListResponse getStatementByCoachId(int page, int perPage, Long coachId) {
        log.info("Get account statement by coach Id{}", coachId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<AccountStatement> statementPage;
        statementPage = accountStatementRepository.findAllByCoach_id(coachId, pageable);

        return new ListResponse(statementPage.getContent(),
                statementPage.getTotalPages(), statementPage.getNumberOfElements(),
                statementPage.getTotalElements());
    }
    // Get account statement by client Id
    public ListResponse getStatementByClientId(int page, int perPage, Long clientId) {
        log.info("Get account statement by client Id{}", clientId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<AccountStatement> statementPage;
        statementPage = accountStatementRepository.findAllByClient_id(clientId, pageable);

        return new ListResponse(statementPage.getContent(),
                statementPage.getTotalPages(), statementPage.getNumberOfElements(),
                statementPage.getTotalElements());
    }
    // Get account statement by coach Id and client Id
    public ListResponse getStatementByCoachIdAndClientId(int page, int perPage, Long coachId, Long clientId) {
        log.info("Get account statement by coach Id{} and client Id{}", coachId, clientId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<AccountStatement> statementPage;
        statementPage = accountStatementRepository.findAllByCoach_idAndClient_id(coachId, clientId, pageable);

        return new ListResponse(statementPage.getContent(),
                statementPage.getTotalPages(), statementPage.getNumberOfElements(),
                statementPage.getTotalElements());
    }
    // Get account statement by organization Id and client Id
    public ListResponse getStatementByOrganizationIdAndClientId(int page, int perPage, Long organizationId, Long clientId) {
        log.info("Get account statement by organization Id{} and client Id{}", organizationId, clientId);

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<AccountStatement> statementPage;
        statementPage = accountStatementRepository.findAllByOrganization_idAndClient_id(organizationId, clientId, pageable);

        return new ListResponse(statementPage.getContent(),
                statementPage.getTotalPages(), statementPage.getNumberOfElements(),
                statementPage.getTotalElements());
    }

}
