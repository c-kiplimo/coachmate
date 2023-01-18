package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Payment;
import com.natujenge.thecouch.web.rest.dto.PaymentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface PaymentRepository extends PagingAndSortingRepository<Payment,Long>, QuerydslPredicateExecutor<Payment> {

    Page<PaymentDto> findAllByCoach_id(Long coachId, Pageable pageable);

    Optional<PaymentDto> findByIdAndCoachId(Long id, Long coachId);

    Optional<Payment> findFirstByCoachIdAndSessionIdOrderByIdDesc(Long coachId, Long sessionId);

    Optional<Payment> getPaymentByIdAndCoachId(Long id, Long coachId);

    Page<PaymentDto> findByCoachIdAndClientId(Long coachId, Long clientId, Pageable pageable);

    Page<PaymentDto> findBySessionId(Long sessionId, Pageable pageable);
}
