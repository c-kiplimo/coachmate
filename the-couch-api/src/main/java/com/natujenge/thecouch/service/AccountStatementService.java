package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.AccountStatement;
import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.AccountStatementRepository;
import com.natujenge.thecouch.repository.ClientWalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AccountStatementService {

    private final AccountStatementRepository accountStatementRepository;

   private final ClientWalletRepository walletRepository;

    public AccountStatementService(AccountStatementRepository accountStatementRepository, @Lazy ClientWalletRepository walletRepository) {
        this.accountStatementRepository = accountStatementRepository;
        this.walletRepository = walletRepository;
    }

    // update account statement
    public void updateAccountStatement(User coach, User client, float amountIn, float walletBalance,
                                       float amountBilled) {
        log.info("Update account statement for client {} by coach {}", client.getId(), coach.getId());

        AccountStatement accountStatement = new AccountStatement();
        accountStatement.setCoach(coach);
        accountStatement.setClient(client);
        accountStatement.setAmountIn(amountIn);
        accountStatement.setBalanceBefore(amountBilled);
        accountStatement.setBalanceAfter(walletBalance);
        accountStatement.setLastUpdatedBy("System");
        accountStatement.setLastUpdatedAt(LocalDateTime.now());
        accountStatement.setDescription("Amount deposited");
        // save account statement
        log.info("saving account statement:{}", accountStatement);
        accountStatementRepository.save(accountStatement);
        log.info("account statement saved");
    }

    public void updateAccountStatementByOrganization(Organization organization, User client, float amountIn, float amountBilled, float paymentBalance) {
    }
}
    // Get account statement by organization Id
//    public ListResponse getStatementByOrganizationId(int page, int perPage, Long organizationId) {
//        log.info("Get account statement by organization Id{}", organizationId);
//
//        page = page - 1;
//        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
//        Pageable pageable = PageRequest.of(page, perPage, sort);
//        // GET BY STATEMENT PERIOD
//        if (statementPeriod == StatementPeriod.PerMonth) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByOrganization_idAndCreatedAtBetween(organizationId,
//                    LocalDateTime.now().minusMonths(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else if (statementPeriod == StatementPeriod.Per6Months) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByOrganization_idAndCreatedAtBetween(organizationId,
//                    LocalDateTime.now().minusWeeks(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else if (statementPeriod == StatementPeriod.PerYear) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByOrganization_idAndCreatedAtBetween(organizationId,
//                    LocalDateTime.now().minusDays(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByOrganization_id(organizationId, pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        }
//    }
//    // Get account statement by coach Id
//    public ListResponse getStatementByCoachId(int page, int perPage, Long coachId) {
//        log.info("Get account statement by coach Id{}", coachId);
//
//        page = page - 1;
//        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
//        Pageable pageable = PageRequest.of(page, perPage, sort);
//        // GET BY STATEMENT PERIOD
//        if (statementPeriod == StatementPeriod.PerMonth) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByCoach_idAndCreatedAtBetween(coachId,
//                    LocalDateTime.now().minusMonths(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else if (statementPeriod == StatementPeriod.Per6Months) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByCoach_idAndCreatedAtBetween(coachId,
//                    LocalDateTime.now().minusWeeks(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else if (statementPeriod == StatementPeriod.PerYear) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByCoach_idAndCreatedAtBetween(coachId,
//                    LocalDateTime.now().minusDays(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByCoach_id(coachId, pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        }
//    }
//    // Get account statement by client Id
//    public ListResponse getStatementByClientId(int page, int perPage, Long clientId) {
//        log.info("Get account statement by client Id{}", clientId);
//
//        page = page - 1;
//        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
//        Pageable pageable = PageRequest.of(page, perPage, sort);
//        // GET BY STATEMENT PERIOD
//        if (statementPeriod == StatementPeriod.PerMonth) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByClient_idAndCreatedAtBetween(clientId,
//                    LocalDateTime.now().minusMonths(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else if (statementPeriod == StatementPeriod.Per6Months) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByClient_idAndCreatedAtBetween(clientId,
//                    LocalDateTime.now().minusWeeks(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else if (statementPeriod == StatementPeriod.PerYear) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByClient_idAndCreatedAtBetween(clientId,
//                    LocalDateTime.now().minusDays(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByClient_id(clientId, pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        }
//
//    }
//    // Get account statement by coach Id and client Id
//    public ListResponse getStatementByCoachIdAndClientId(int page, int perPage, Long coachId, Long clientId) {
//        log.info("Get account statement by coach Id{} and client Id{}", coachId, clientId);
//
//        page = page - 1;
//        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
//        Pageable pageable = PageRequest.of(page, perPage, sort);
//        // GET BY STATEMENT PERIOD
//        if (statementPeriod == StatementPeriod.PerMonth) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByCoach_idAndClient_idAndCreatedAtBetween(coachId, clientId,
//                    LocalDateTime.now().minusMonths(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else if (statementPeriod == StatementPeriod.Per6Months) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByCoach_idAndClient_idAndCreatedAtBetween(coachId, clientId,
//                    LocalDateTime.now().minusWeeks(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else if (statementPeriod == StatementPeriod.PerYear) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByCoach_idAndClient_idAndCreatedAtBetween(coachId, clientId,
//                    LocalDateTime.now().minusDays(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByCoach_idAndClient_id(coachId, clientId, pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        }
//
//    }
//    // Get account statement by organization Id and client Id
//    public ListResponse getStatementByOrganizationIdAndClientId(int page, int perPage, Long organizationId, Long clientId) {
//        log.info("Get account statement by organization Id{} and client Id{}", organizationId, clientId);
//
//        page = page - 1;
//        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
//        Pageable pageable = PageRequest.of(page, perPage, sort);
//        // GET BY STATEMENT PERIOD
//        if (statementPeriod == StatementPeriod.PerMonth) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByOrganization_idAndClient_idAndCreatedAtBetween(organizationId, clientId,
//                    LocalDateTime.now().minusMonths(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else if (statementPeriod == StatementPeriod.Per6Months) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByOrganization_idAndClient_idAndCreatedAtBetween(organizationId, clientId,
//                    LocalDateTime.now().minusWeeks(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else if (statementPeriod == StatementPeriod.PerYear) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByOrganization_idAndClient_idAndCreatedAtBetween(organizationId, clientId,
//                    LocalDateTime.now().minusDays(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByOrganization_idAndClient_id(organizationId, clientId, pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        }
//    }
//
//    public ListResponse getStatementByOrganizationIdAndClientIdAndStatementPeriod(int page, int perPage, Long organizationId, Long clientId, StatementPeriod statementPeriod) {
//        log.info("Get account statement by organization Id{} and client Id{} and statement period{}", organizationId, clientId, statementPeriod);
//
//        page = page - 1;
//        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
//        Pageable pageable = PageRequest.of(page, perPage, sort);
//        // GET BY STATEMENT PERIOD
//        if (statementPeriod == StatementPeriod.PerMonth) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByOrganization_idAndClient_idAndCreatedAtBetween(organizationId, clientId,
//                    LocalDateTime.now().minusMonths(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else if (statementPeriod == StatementPeriod.Per6Months) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByOrganization_idAndClient_idAndCreatedAtBetween(organizationId, clientId,
//                    LocalDateTime.now().minusWeeks(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else if (statementPeriod == StatementPeriod.PerYear) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByOrganization_idAndClient_idAndCreatedAtBetween(organizationId, clientId,
//                    LocalDateTime.now().minusDays(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByOrganization_idAndClient_id(organizationId, clientId, pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        }
//    }
//
//    public ListResponse getStatementByCoachIdAndClientIdAndStatementPeriod(int page, int perPage, Long coachId, Long clientId, StatementPeriod statementPeriod) {
//        log.info("Get account statement by coach Id{} and client Id{} and statement period{}", coachId, clientId, statementPeriod);
//
//        page = page - 1;
//        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
//        Pageable pageable = PageRequest.of(page, perPage, sort);
//        // GET BY STATEMENT PERIOD
//        if (statementPeriod == StatementPeriod.PerMonth) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByCoach_idAndClient_idAndCreatedAtBetween(coachId, clientId,
//                    LocalDateTime.now().minusMonths(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else if (statementPeriod == StatementPeriod.Per6Months) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByCoach_idAndClient_idAndCreatedAtBetween(coachId, clientId,
//                    LocalDateTime.now().minusWeeks(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else if (statementPeriod == StatementPeriod.PerYear) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByCoach_idAndClient_idAndCreatedAtBetween(coachId, clientId,
//                    LocalDateTime.now().minusDays(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByCoach_idAndClient_id(coachId, clientId, pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        }
//    }
//
//    public ListResponse getStatementByCoachIdAndStatementPeriod(int page, int perPage, Long coachId, StatementPeriod statementPeriod) {
//        log.info("Get account statement by coach Id{} and statement period{}", coachId, statementPeriod);
//
//        page = page - 1;
//        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
//        Pageable pageable = PageRequest.of(page, perPage, sort);
//        // GET BY STATEMENT PERIOD
//        if (statementPeriod == StatementPeriod.PerMonth) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByCoach_idAndCreatedAtBetween(coachId,
//                    LocalDateTime.now().minusMonths(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else if (statementPeriod == StatementPeriod.Per6Months) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByCoach_idAndCreatedAtBetween(coachId,
//                    LocalDateTime.now().minusWeeks(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else if (statementPeriod == StatementPeriod.PerYear) {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByCoach_idAndCreatedAtBetween(coachId,
//                    LocalDateTime.now().minusDays(1), LocalDateTime.now(), pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        } else {
//            Page<AccountStatement> statementPage;
//            statementPage = accountStatementRepository.findAllByCoach_id(coachId, pageable);
//            return new ListResponse(statementPage.getContent(),
//                    statementPage.getTotalPages(), statementPage.getNumberOfElements(),
//                    statementPage.getTotalElements());
//        }
//    }
//
//    public void updateAccountStatementByOrganization(Organization organization, Client client, float amountIn, float amountBilled, float paymentBalance) {
//    }
//}
