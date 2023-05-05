package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Session;
import com.natujenge.thecouch.service.dto.SessionDTO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface SessionRepository extends JpaRepository<Session,Long>, JpaSpecificationExecutor<Session> {
    Long countByContractId(Long contractId);

    List<Session> findAllBySessionSchedulesSessionDate(LocalDate date);
}
