package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Coach;
import com.zaxxer.hikari.util.FastList;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import com.natujenge.thecouch.repository.NotificationSettingsRepository;
import com.natujenge.thecouch.domain.NotificationSettings;
import com.natujenge.thecouch.web.rest.dto.NotificationSettingsDto;
import com.natujenge.thecouch.web.rest.request.NotificationSettingsRequest;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;

@Slf4j
@Service
@Data
public class NotificationSettingsService {
    private final NotificationSettingsRepository notificationSettingsRepository;
    private final CoachService coachService;

    private final UserService userService;

    public NotificationSettingsService(NotificationSettingsRepository notificationSettingsRepository, CoachService coachService, UserService userService) {
        this.notificationSettingsRepository = notificationSettingsRepository;
        this.coachService = coachService;
        this.userService = userService;
    }

//    public Optional<NotificationSettingsDto> getAllNotifications(Long coachId) {
//        log.info("Getting Notifications Settings");
//        // Get All Notifications By coachId
//        return notificationSettingsRepository.findAllByCoachId(coachId);
//    }


    public NotificationSettings updateSettings(NotificationSettingsRequest notificationSettingsRequest, Long coachId, String coachName) {
        log.info("Updating Settings for Baker with id {}", coachId);
        Optional<NotificationSettings> notificationOptional = notificationSettingsRepository.findByCoachId(coachId);

        if (notificationOptional.isEmpty()) {
            throw new IllegalArgumentException("Baker has no setting configured! Contact Admin");
        }

        NotificationSettings NotificationSettings = notificationOptional.get();

        // smsDisplayName
        if (notificationSettingsRequest.getSmsDisplayName() != null &&
                notificationSettingsRequest.getSmsDisplayName().length() > 0 &&
                !Objects.equals(NotificationSettings.getSmsDisplayName(),
                        notificationSettingsRequest.getSmsDisplayName())) {
            NotificationSettings.setSmsDisplayName(notificationSettingsRequest.getSmsDisplayName());
        }
        // emailDisplayName
        if (notificationSettingsRequest.getEmailDisplayName() != null &&
                notificationSettingsRequest.getEmailDisplayName().length() > 0 &&
                !Objects.equals(NotificationSettings.getEmailDisplayName(),
                        notificationSettingsRequest.getEmailDisplayName())) {
            NotificationSettings.setSmsDisplayName(notificationSettingsRequest.getEmailDisplayName());
        }
        // notificationMode
        if (notificationSettingsRequest.getNotificationMode() != null && !Objects.equals(NotificationSettings.getNotificationMode(),
                notificationSettingsRequest.getNotificationMode())) {
            NotificationSettings.setNotificationMode(notificationSettingsRequest.getNotificationMode());
        }

        // Payment Settings
        // payment

        // msisdn
        if (notificationSettingsRequest.getMsisdn() != null &&
                notificationSettingsRequest.getMsisdn().length() > 0 &&
                !Objects.equals(NotificationSettings.getMsisdn(),
                        notificationSettingsRequest.getMsisdn())) {
            NotificationSettings.setMsisdn(notificationSettingsRequest.getMsisdn());
        }
        // tillNumber
        if (notificationSettingsRequest.getTillNumber() != null &&
                notificationSettingsRequest.getTillNumber().length() > 0 &&
                !Objects.equals(NotificationSettings.getTillNumber(),
                        notificationSettingsRequest.getTillNumber())) {
            NotificationSettings.setTillNumber(notificationSettingsRequest.getTillNumber());
        }
        // accountNumber
        if (notificationSettingsRequest.getAccountNumber() != null &&
                notificationSettingsRequest.getAccountNumber().length() > 0 &&
                !Objects.equals(NotificationSettings.getAccountNumber(),
                        notificationSettingsRequest.getAccountNumber())) {
            NotificationSettings.setAccountNumber(notificationSettingsRequest.getAccountNumber());
        }
        // depositPercentage


        // contract settings
        // Enable notifications
        if (notificationSettingsRequest.isNewContractEnable()) {
            NotificationSettings.setNewContractEnable(notificationSettingsRequest.isNewContractEnable());
        }
        if (notificationSettingsRequest.isPartialBillPaymentEnable()) {
            NotificationSettings.setPartialBillPaymentEnable(
                    notificationSettingsRequest.isPartialBillPaymentEnable());
        }
        if (notificationSettingsRequest.isFullBillPaymentEnable()) {
            NotificationSettings.setFullBillPaymentEnable(
                    notificationSettingsRequest.isFullBillPaymentEnable());
        }
        if (notificationSettingsRequest.isCancelSessionEnable()) {
            NotificationSettings.setCancelSessionEnable(notificationSettingsRequest.isCancelSessionEnable());
        }
        if (notificationSettingsRequest.isConductedSessionEnable()) {
            NotificationSettings.setConductedSessionEnable(notificationSettingsRequest.isConductedSessionEnable());
        }

        // smsTemplate
        if (notificationSettingsRequest.getNewContractTemplate() != null &&
                notificationSettingsRequest.getNewContractTemplate().length() > 0 &&
                !Objects.equals(NotificationSettings.getNewContractTemplate(),
                        notificationSettingsRequest.getNewContractTemplate())) {
            NotificationSettings.setNewContractTemplate(notificationSettingsRequest.getNewContractTemplate());
        }

        if (notificationSettingsRequest.getPaymentReminderTemplate() != null &&
                notificationSettingsRequest.getPaymentReminderTemplate().length() > 0) {
            NotificationSettings.setPaymentReminderTemplate(notificationSettingsRequest.getPaymentReminderTemplate());
        }

        if (notificationSettingsRequest.getPartialBillPaymentTemplate() != null &&
                notificationSettingsRequest.getPartialBillPaymentTemplate().length() > 0 &&
                !Objects.equals(NotificationSettings.getPartialBillPaymentTemplate(),
                        notificationSettingsRequest.getPartialBillPaymentTemplate())) {
            NotificationSettings.setPartialBillPaymentTemplate(
                    notificationSettingsRequest.getPartialBillPaymentTemplate());
        }

        if (notificationSettingsRequest.getFullBillPaymentTemplate() != null &&
                notificationSettingsRequest.getFullBillPaymentTemplate().length() > 0 &&
                !Objects.equals(NotificationSettings.getFullBillPaymentTemplate(),
                        notificationSettingsRequest.getFullBillPaymentTemplate())) {
            NotificationSettings.setFullBillPaymentTemplate(
                    notificationSettingsRequest.getFullBillPaymentTemplate());
        }

        if (notificationSettingsRequest.getCancelSessionTemplate() != null &&
                notificationSettingsRequest.getCancelSessionTemplate().length() > 0 &&
                !Objects.equals(NotificationSettings.getCancelSessionTemplate(),
                        notificationSettingsRequest.getCancelSessionTemplate())) {
            NotificationSettings.setCancelSessionTemplate(notificationSettingsRequest.getCancelSessionTemplate());
        }

        if (notificationSettingsRequest.getConductedSessionTemplate() != null &&
                notificationSettingsRequest.getConductedSessionTemplate().length() > 0 &&
                !Objects.equals(NotificationSettings.getConductedSessionTemplate(),
                        notificationSettingsRequest.getConductedSessionTemplate())) {
            NotificationSettings.setConductedSessionTemplate(notificationSettingsRequest.getConductedSessionTemplate());
        }

        NotificationSettings.setLastUpdatedAt(LocalDateTime.now());
        log.info("Settings Updated Successfully");

        return notificationSettingsRepository.save(NotificationSettings);

    }

    public NotificationSettings addNewSettings(NotificationSettingsRequest notificationSettingsRequest,
                                               String fullName, Long coachId) {

        log.info("Creating Notifications for coach with Id {}", coachId);

        // Get coach
        FastList<Object> OptionalCoach = null;
        if (OptionalCoach.isEmpty()) {
            throw new IllegalArgumentException("Coach with Id " + coachId + " does not Exist");
        }

            NotificationSettings notificationSettings = new NotificationSettings();
            notificationSettings.setNotificationMode(notificationSettingsRequest.getNotificationMode());
            notificationSettings.setSmsDisplayName(notificationSettingsRequest.getSmsDisplayName());
            notificationSettings.setEmailDisplayName(notificationSettingsRequest.getEmailDisplayName());
            notificationSettings.setNotificationEnable(notificationSettingsRequest.isNotificationEnable());
            notificationSettings.setMsisdn(notificationSettingsRequest.getMsisdn());
            notificationSettings.setTillNumber(notificationSettingsRequest.getTillNumber());
            notificationSettings.setAccountNumber(notificationSettingsRequest.getAccountNumber());
            notificationSettings.setSessionTemplateType(notificationSettingsRequest.getSessionTemplateType());

            // Templates
            notificationSettings.setNewContractTemplate(notificationSettingsRequest.getNewContractTemplate());
            notificationSettings.setPartialBillPaymentTemplate(
                    notificationSettingsRequest.getPartialBillPaymentTemplate());
            notificationSettings.setFullBillPaymentTemplate(notificationSettingsRequest.getFullBillPaymentTemplate());
            notificationSettings.setCancelSessionTemplate(notificationSettingsRequest.getCancelSessionTemplate());
            notificationSettings.setConductedSessionTemplate(notificationSettingsRequest.getConductedSessionTemplate());
            notificationSettings.setPaymentReminderTemplate((notificationSettingsRequest.getPaymentReminderTemplate()));

            // Enable
            notificationSettings.setNewContractEnable(notificationSettingsRequest.isNewContractEnable());
            notificationSettings.setPartialBillPaymentEnable(notificationSettingsRequest.isPartialBillPaymentEnable());
            notificationSettings.setFullBillPaymentEnable(notificationSettingsRequest.isFullBillPaymentEnable());
            notificationSettings.setCancelSessionEnable(notificationSettingsRequest.isCancelSessionEnable());
            notificationSettings.setConductedSessionEnable(notificationSettingsRequest.isConductedSessionEnable());

            // relations
            Coach coach = new Coach();
            notificationSettings.setCoach(coach);
            notificationSettings.setCreatedAt(LocalDateTime.now());
            notificationSettings.setCreatedBy(fullName);

            // TODO: Link to User (Not Necessary) -> Default way of adding notifications is during registration
            return notificationSettingsRepository.save(notificationSettings);
        }

    public Optional<NotificationSettings> getNotification(Long coachId) {
        return notificationSettingsRepository.findByCoachId(coachId);
    }
}