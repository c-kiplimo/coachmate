package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long>{

    PaymentDetails findTopByCoachId(Long coachId);
}