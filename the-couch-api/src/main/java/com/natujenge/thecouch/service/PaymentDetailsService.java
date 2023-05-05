package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.PaymentDetails;
import com.natujenge.thecouch.repository.PaymentDetailsRepository;
import com.natujenge.thecouch.security.SecurityUtils;
import com.natujenge.thecouch.service.mapper.PaymentDetailsMapper;
import com.natujenge.thecouch.web.rest.dto.PaymentDetailsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PaymentDetailsService {
    private final PaymentDetailsRepository paymentDetailsRepository;
    private final PaymentDetailsMapper paymentDetailsMapper;

    public PaymentDetailsService(PaymentDetailsRepository paymentDetailsRepository, PaymentDetailsMapper paymentDetailsMapper) {
        this.paymentDetailsRepository = paymentDetailsRepository;
        this.paymentDetailsMapper = paymentDetailsMapper;
    }


    public PaymentDetailsDTO save(PaymentDetailsDTO paymentDetailsDTO){
        log.info("Request to save paymentDetails: {}", paymentDetailsDTO);
        if (paymentDetailsDTO.getId() == null){
            paymentDetailsDTO.setCreatedAt(LocalDateTime.now());
            paymentDetailsDTO.setCreatedBy(SecurityUtils.getCurrentUsername());
        } else {
            paymentDetailsDTO.setLastUpdatedAt(LocalDateTime.now());
            paymentDetailsDTO.setLastUpdatedBy(SecurityUtils.getCurrentUsername());
        }

        PaymentDetails paymentDetails = paymentDetailsMapper.toEntity(paymentDetailsDTO);
        paymentDetails= paymentDetailsRepository.save(paymentDetails);
        return paymentDetailsMapper.toDto(paymentDetails);
    }
    public PaymentDetailsDTO findTopByCoachId(Long coachId){
        log.info("Request to find one payment details by coachId:{}", coachId);

        return paymentDetailsMapper.toDto(paymentDetailsRepository.findTopByCoachId(coachId));
    }
}
