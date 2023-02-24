package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.AccountStatement;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountStatementRepository extends PagingAndSortingRepository<AccountStatement, Long> {

}
