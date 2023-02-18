package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.CoachingCategory;

import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.ClientRepository;
import com.natujenge.thecouch.repository.ContractObjectiveRepository;
import com.natujenge.thecouch.repository.ContractRepository;
import com.natujenge.thecouch.repository.SessionRepository;
import com.natujenge.thecouch.service.notification.NotificationServiceHTTPClient;
import com.natujenge.thecouch.util.NotificationUtil;
import com.natujenge.thecouch.web.rest.dto.ContractDto;
import com.natujenge.thecouch.web.rest.request.ContractRequest;
import com.natujenge.thecouch.web.rest.request.NotificationRequest;
import com.natujenge.thecouch.web.rest.request.SessionRequest;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@Data
public class ContractService {
    public ContractDto contractDto;
    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    ContractObjectiveRepository clientObjectiveRepository;

    @Autowired
    ContractRepository contractRepository;


    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CoachService coachService;
    @Autowired
    private NotificationServiceHTTPClient notificationServiceHTTPClient;
    @Autowired
    NotificationSettingsService notificationSettingsService;

    @Autowired
    ClientBillingAccountService clientBillingAccountService;


    @Autowired
    NotificationService notificationService;

    @Autowired
    ContractObjectiveRepository contractObjectiveRepository;

    public List<Contract> getContracts(Long coachId) {
        return contractRepository.findAllByCoachId(coachId);
    }

    public Contract getSingleContract(Long contractId) {

        // Verify Coach

        Optional<Contract> optionalContract = contractRepository.findById(contractId);

        if (optionalContract.isEmpty()){
            throw new IllegalArgumentException("Contract with Id "+contractId+" does not exist!");
        }
        return optionalContract.get();
    }
    public List<Contract> getContractByClientId(Long clientId) {
        return contractRepository.findAllByClientId(clientId);
    }

    public Contract createContract(Long coachId,ContractRequest contractRequest) {

        // Get Client
        Client client = clientRepository.findById(contractRequest.getClientId()).orElseThrow(() -> new UserNotFoundException("Client by id " + contractRequest.getClientId()
                + " not found"));

        // Get Coach
        Coach coach = coachService.findCoachById(coachId);
        // Save Contract
        Contract contract = new Contract();

        contract.setCoachingTopic(contractRequest.getCoachingTopic());
        contract.setCoachingCategory(contractRequest.getCoachingCategory());
        contract.setStartDate(contractRequest.getStartDate());
        contract.setEndDate((contractRequest.getEndDate()));
        contract.setIndividualFeesPerSession(contractRequest.getIndividualFeesPerSession());
        contract.setGroupFeesPerSession(contractRequest.getGroupFeesPerSession());
        contract.setNoOfSessions(contractRequest.getNoOfSessions());




        Float  amountDue = (contractRequest.getCoachingCategory() == CoachingCategory.INDIVIDUAL)?
                contractRequest.getIndividualFeesPerSession() * contract.getNoOfSessions():
                contractRequest.getGroupFeesPerSession() * contract.getNoOfSessions();
    log.info("Amount Due:{} ",amountDue);
        contract.setAmountDue(amountDue);
        clientBillingAccountService.updateBillingAccount(amountDue,coach,client);
        log.info("Amount Due:{} ",amountDue);
        log.info("Contract: "+contract.toString());
        log.info("Client: "+client.toString());
        log.info("Coach: "+coach.toString());
        contract.setClient(client);
        contract.setCoach(coach);
        if(coach.getOrgIdId()!=null){
            contract.setOrgId(coach.getOrgIdId());
        }

        log.info("Contract: "+contract.toString());

        Contract contract1 = contractRepository.save(contract);

        List<String> objectives = contractRequest.getObjectives();
        // save Objectives
        List<CoachingObjective> coachingObjectives = new ArrayList<>();

        for (String objective:
             objectives) {
            CoachingObjective coachingObjective = new CoachingObjective();
            coachingObjective.setObjective(objective);
            coachingObjective.setCreatedBy(coach.getFullName());
            coachingObjective.setLastUpdatedBy(coach.getFullName());

            coachingObjective.setClient(client);
            coachingObjective.setContract(contract1);
            coachingObjective.setCoach(coach);

            coachingObjectives.add(coachingObjective);
        }
        contractObjectiveRepository.saveAll(coachingObjectives);
        if (contractRequest.isSendNotification()) {
            // check notificationType from dB
            Optional<NotificationSettings> optionalNotificationSettings = notificationSettingsService.
                    getNotification(coachId);
            log.info("Obtained NotificationDTO");
            if (optionalNotificationSettings.isEmpty()) {
                throw new IllegalStateException("Notification Not Found!");
            }
            NotificationSettings NotificationSettings = optionalNotificationSettings.get();
            Map<String, Object> replacementVariables = new HashMap<>();
            replacementVariables.put("client_name",contract1.getCoach().getFullName());
            replacementVariables.put("coachingTopic", contract1.getCoachingTopic());
            String[] startDate = contract1.getStartDate().toString().split("T");
            String contractStartDate = startDate[0] + " at "
                    + startDate[1];
            replacementVariables.put("startDate",contractStartDate);
            String[] endDate = contract1.getEndDate().toString().split("T");
            String contractEndDate = endDate[0] + " at "
                    + endDate[1];
            replacementVariables.put("endDate",contractEndDate);
            replacementVariables.put("business_name", contract1.getCoach().getBusinessName());
            // Check if Notifications are allowed
            if ((NotificationSettings.isNotificationEnable() && NotificationSettings.isNewContractEnable()) ||
                    contractRequest.isSendNotification()) {

                log.info(" Sending Notification to Client");
                if (NotificationSettings.getNotificationMode() == NotificationMode.SMS) {
                    // Default
                    String smsTemplate = NotificationSettings.getNewContractTemplate();
                    //replacement to get content
                    String smsContent = NotificationUtil.generateContentFromTemplate(smsTemplate, replacementVariables);
                    // SHORTCODE
                    String sourceAddress = NotificationSettings.getSmsDisplayName();
                    String referenceId = contract1.getId().toString();
                    String msisdn = client.getMsisdn();

                    log.info("about to send message to client content: {}, from: {}, to: {}, ref id {}",
                            smsContent, sourceAddress, msisdn, referenceId);
                    //send sms
                    log.info("Sending notification to client");
                    notificationServiceHTTPClient.sendSMS(sourceAddress, msisdn, smsContent, referenceId);
                    log.info("sms sent ");
                    // Store notification object to DB
                    NotificationRequest notificationRequest = new NotificationRequest();
                    notificationRequest.setNotificationMode(NotificationSettings.getNotificationMode());

                    notificationRequest.setSubject("NEW CONTRACT");
                    // ShortCode
                    notificationRequest.setSrcAddress(NotificationSettings.getSmsDisplayName());
                    notificationRequest.setContent(smsContent);
                    notificationRequest.setSendReason(contract.getCoachingTopic());
                    notificationRequest.setClientId(client.getId());
                    notificationRequest.setContractId(contract1.getId());

                    // Invoke notificationService to save notification
                    notificationService.createNotificationOnContractCreation(notificationRequest,contract,coach);

                } else {
                    // Send Email
                    log.info("Sending Email");
                }
            } else {
                log.info("Notifications Not allowed! Notifications not sent");
            }

        }
        return contract1;

    }

    public void deleteContract(Long coachId, Long contractId) {

        // GetContract ById and CoachId
        Contract contract = getSingleContract(contractId);

        contractRepository.delete(contract);
    }


    public List<Contract> getContractByOrgId(Long orgId) {
        return contractRepository.findContractByOrgId(orgId);
    }
}
