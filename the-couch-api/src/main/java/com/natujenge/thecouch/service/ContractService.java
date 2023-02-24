package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.CoachingCategory;

import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.domain.enums.PaymentStatus;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.*;
import com.natujenge.thecouch.service.notification.NotificationServiceHTTPClient;
import com.natujenge.thecouch.util.NotificationHelper;
import com.natujenge.thecouch.util.NotificationUtil;
import com.natujenge.thecouch.web.rest.dto.ClientDto;
import com.natujenge.thecouch.web.rest.dto.ContractDto;
import com.natujenge.thecouch.web.rest.request.ContractRequest;
import com.natujenge.thecouch.web.rest.request.NotificationRequest;
import com.natujenge.thecouch.web.rest.request.SessionRequest;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    NotificationRepository notificationRepository;


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
        if(coach.getOrganization()!=null){
            contract.setOrganization(coach.getOrganization());
        }

        log.info("Contract: "+contract.toString());
        //check client wallet balance

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

        log.info("Prep to send sms");
        Map<String, Object> replacementVariables = new HashMap<>();
        replacementVariables.put("client_name", contract1.getClient().getFullName());
        replacementVariables.put("coaching_topic", contract1.getCoachingTopic());
        replacementVariables.put("start_date",contract1.getStartDate());
        replacementVariables.put("end_date",contract1.getEndDate());
        replacementVariables.put("business_name", contract1.getClient().getCoach().getBusinessName());

        String smsTemplate = Constants.DEFAULT_NEW_CONTRACT_SMS_TEMPLATE;

        //replacement to get content
        String smsContent = NotificationUtil.generateContentFromTemplate(smsTemplate, replacementVariables);
        String sourceAddress = Constants.DEFAULT_SMS_SOURCE_ADDRESS; //TO-DO get this value from cooperative settings
        String referenceId = contract1.getId().toString();
        String msisdn = contract1.getClient().getMsisdn();

        log.info("about to send message to Client content: {}, from: {}, to: {}, ref id {}", smsContent, sourceAddress, msisdn, referenceId);

        //send sms
        notificationServiceHTTPClient.sendSMS(sourceAddress, msisdn, smsContent, referenceId);
        log.info("sms sent ");

        // sendEmail
        notificationServiceHTTPClient.sendEmail(client.getEmail(),"Contract Created",smsContent,false);
        log.info("Email sent");


        //create notification object and send it
        Notification notification = new Notification();
        notification.setNotificationMode(NotificationMode.SMS);
        notification.setDestinationAddress(msisdn);
        notification.setSourceAddress(sourceAddress);
        notification.setContent(smsContent);
        notification.setCreatedBy(coach.getFullName());
        //TO DO: add logic to save notification to db

        notificationRepository.save(notification);
        return contract1;
    }

    public void deleteContract(Long coachId, Long contractId) {

        // GetContract ById and CoachId
        Contract contract = getSingleContract(contractId);

        contractRepository.delete(contract);
    }


    public List<Contract> getContractByOrgId(Long organizationId) {
        return contractRepository.findContractByOrganizationId(organizationId);
    }

    public Contract getContract(Long contractId) {
        return contractRepository.findById(contractId).orElseThrow(() -> new UserNotFoundException("Contract by id " + contractId
                + " not found"));
    }
}
