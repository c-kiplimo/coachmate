package com.natujenge.thecouch.web.rest.dto;


import com.natujenge.thecouch.domain.enums.MPesaPaymentType;
import com.natujenge.thecouch.domain.enums.PaymentType;

import java.time.LocalDateTime;

public class CoachPaymentDetailsDTO {
    private Long id;
    private PaymentType paymentType;
    private MPesaPaymentType mpesaPaymentType;
    private String msisdn; //send money & pochi
    private String tillNumber;//buy goods
    private String bankName; //bank
    private String bankBranchName; //bank
    private String accountName; //bank
    private String businessNumber; //pay bill
    private String accountNumber; // bank & pay bill
    private float depositPercentage;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;

}
