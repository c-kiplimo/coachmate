package com.natujenge.thecouch.web.rest.dto;


import com.natujenge.thecouch.domain.enums.ClientType;
import com.natujenge.thecouch.domain.enums.ModeOfPayment;
import com.natujenge.thecouch.domain.enums.PaymentCurrency;
import com.natujenge.thecouch.domain.enums.StatementPeriod;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ClientWalletDTO {

    public String description;
    private Long id;
    private Float walletBalanceBefore;
    private Float walletBalance;
    private Float amountDeposited;
    private Float amountBilled;
    private String clientWalletNumber;
    private String extPaymentRef;
    private ModeOfPayment modeOfPayment;
    private PaymentCurrency paymentCurrency;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDate lastUpdatedAt;
    private StatementPeriod statementPeriod;
    private String lastUpdatedBy;
    private Long clientId;
    private String clientFullName;
    private ClientType clientType;
    private String clientMsisdn;
    private Long coachId;
    private Long organizationId;
}
