package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Session;
import org.modelmapper.Converters;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long>, JpaSpecificationExecutor<Session> {
    Long countByContractId(Long contractId);

    List<Session> findAllBySessionSchedulesSessionDate(LocalDate date);

}
