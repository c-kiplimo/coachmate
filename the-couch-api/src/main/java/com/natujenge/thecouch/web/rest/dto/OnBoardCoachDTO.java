package com.natujenge.thecouch.web.rest.dto;

import com.natujenge.thecouch.domain.enums.MPesaPaymentType;
import com.natujenge.thecouch.domain.enums.PaymentType;
import lombok.Data;

@Data
public class OnBoardCoachDTO {
    private Long coachId;
    private String depositPercentage;
    private String physicalAddress;
    private String postalAddress;
    private PaymentType paymentType;
    private MPesaPaymentType mpesaPaymentType;
    private String tillNumber;
    private String msisdn;
    private String businessNumber;
    private String accountNumber;
    private String newContractTemplate;
    private String partialBillPaymentTemplate;
    private String fullBillPaymentTemplate;
    private String cancelSessionTemplate;
    private String conductedSessionTemplate;
    private String rescheduleSessionTemplate;
    private String paymentReminderTemplate;
    private boolean newContractEnable;
    private boolean rescheduleSessionEnable;
    private boolean partialBillPaymentEnable;
    private boolean fullBillPaymentEnable;
    private boolean cancelSessionEnable;
    private boolean conductedSessionEnable;
    private boolean paymentReminderEnable;
    private String filename;


}
