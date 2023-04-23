package com.natujenge.thecouch.util;

import com.natujenge.thecouch.domain.NotificationSettings;
import com.natujenge.thecouch.domain.enums.SessionTemplateType;
import com.natujenge.thecouch.service.dto.*;
public class OnBoardCoachUtil {
    public static NotificationSettingsDTO extractNotificationSettings(OnBoardCoachDTO onBoardCoachDTO, CoachDTO coachDTO, NotificationSettingsDTO notificationSettingsDTO){
        notificationSettingsDTO.setTillNumber(onBoardCoachDTO.getTillNumber());
        notificationSettingsDTO.setNewContractTemplate(onBoardCoachDTO.getNewContractTemplate());
        notificationSettingsDTO.setPartialBillPaymentTemplate(onBoardCoachDTO.getPartialBillPaymentTemplate());
        notificationSettingsDTO.setFullBillPaymentTemplate(onBoardCoachDTO.getFullBillPaymentTemplate());
        notificationSettingsDTO.setConductedSessionTemplate(onBoardCoachDTO.getConductedSessionTemplate());
        notificationSettingsDTO.setCancelSessionTemplate(onBoardCoachDTO.getCancelSessionTemplate());
        notificationSettingsDTO.setRescheduleSessionTemplate(onBoardCoachDTO.getRescheduleSessionTemplate());
        notificationSettingsDTO.setPaymentReminderTemplate(onBoardCoachDTO.getPaymentReminderTemplate());
        notificationSettingsDTO.setNewContractEnable(onBoardCoachDTO.isNewContractEnable());
        notificationSettingsDTO.setPartialBillPaymentEnable(onBoardCoachDTO.isPartialBillPaymentEnable());
        notificationSettingsDTO.setFullBillPaymentEnable(onBoardCoachDTO.isFullBillPaymentEnable());
        notificationSettingsDTO.setConductedSessionEnable(onBoardCoachDTO.isConductedSessionEnable());
        notificationSettingsDTO.setCancelSessionEnable(onBoardCoachDTO.isCancelSessionEnable());
        notificationSettingsDTO.setRescheduleSessionEnable(onBoardCoachDTO.isRescheduleSessionEnable());
        notificationSettingsDTO.setPaymentReminderEnable(onBoardCoachDTO.isPaymentReminderEnable());
        notificationSettingsDTO.setCoach(coachDTO);

        return notificationSettingsDTO;
    }

    public static PaymentDetailsDTO extractPaymentData(OnBoardCoachDTO onBoardCoachDTO, CoachDTO coachDTO, PaymentDetailsDTO paymentDetailsDTO){
        paymentDetailsDTO.setAccountName(onBoardCoachDTO.getAccountNumber());

        paymentDetailsDTO.setPaymentType(onBoardCoachDTO.getPaymentType());
        paymentDetailsDTO.setMpesaPaymentType(onBoardCoachDTO.getMpesaPaymentType());
        paymentDetailsDTO.setTillNumber(onBoardCoachDTO.getTillNumber());
        paymentDetailsDTO.setAccountNumber(onBoardCoachDTO.getAccountNumber());
        paymentDetailsDTO.setMsisdn(onBoardCoachDTO.getMsisdn());
        paymentDetailsDTO.setBusinessNumber(onBoardCoachDTO.getBusinessNumber());
        paymentDetailsDTO.setCoach(coachDTO);

        return paymentDetailsDTO;
    }

    public static CoachSettingsDTO extractSettingsData(OnBoardCoachDTO onBoardCoachDTO, CoachDTO coachDTO, CoachSettingsDTO coachSettingsDTO){
        coachSettingsDTO.setLogo(onBoardCoachDTO.getFilename());
        coachSettingsDTO.setCoach(coachDTO);

        return coachSettingsDTO;
    }
}

