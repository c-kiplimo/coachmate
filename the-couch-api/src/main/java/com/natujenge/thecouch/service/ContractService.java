package com.natujenge.thecouch.service;

import com.natujenge.thecouch.config.Constants;
import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.CoachingCategory;
import com.natujenge.thecouch.domain.enums.ContractStatus;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.*;
import com.natujenge.thecouch.service.notification.NotificationServiceHTTPClient;
import com.natujenge.thecouch.util.NotificationUtil;
import com.natujenge.thecouch.web.rest.dto.ContractDto;
import com.natujenge.thecouch.web.rest.request.ContractRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@Data
public class ContractService {
    public ContractDto contractDto;


    private final SessionRepository sessionRepository;

    private final OrganizationService organizationService;


    private final ContractObjectiveRepository clientObjectiveRepository;


    private final ContractRepository contractRepository;


    private final UserService userService;


    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    private final NotificationServiceHTTPClient notificationServiceHTTPClient;

    private final NotificationSettingsService notificationSettingsService;

    private final ClientBillingAccountService clientBillingAccountService;


    private final NotificationService notificationService;


    private final ContractObjectiveRepository contractObjectiveRepository;

    private final CoachBillingAccountService coachBillingAccountService;

    public ContractService(SessionRepository sessionRepository, OrganizationService organizationService,
                           ContractObjectiveRepository clientObjectiveRepository, ContractRepository contractRepository
                           , UserService userService, NotificationRepository notificationRepository,
                           UserRepository userRepository, NotificationServiceHTTPClient notificationServiceHTTPClient,
                           NotificationSettingsService notificationSettingsService, ClientBillingAccountService clientBillingAccountService,
                           NotificationService notificationService,
                           ContractObjectiveRepository contractObjectiveRepository, CoachBillingAccountService coachBillingAccountService) {
        this.sessionRepository = sessionRepository;
        this.organizationService = organizationService;
        this.clientObjectiveRepository = clientObjectiveRepository;
        this.contractRepository = contractRepository;

        this.userService = userService;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.notificationServiceHTTPClient = notificationServiceHTTPClient;
        this.notificationSettingsService = notificationSettingsService;
        this.clientBillingAccountService = clientBillingAccountService;
        this.notificationService = notificationService;
        this.contractObjectiveRepository = contractObjectiveRepository;
        this.coachBillingAccountService = coachBillingAccountService;
    }

    public List<Contract> getContracts(Long coachId) {
        return contractRepository.findAllByCoachId(coachId);
    }

    public Contract getSingleContract(Long contractId) {

        // Verify Coach

        Optional<Contract> optionalContract = contractRepository.findById(contractId);

        if (optionalContract.isEmpty()) {
            throw new IllegalArgumentException("Contract with Id " + contractId + " does not exist!");
        }
        return optionalContract.get();
    }

    public List<Contract> getContractByClientId(Long clientId) {
        return contractRepository.findAllByClientId(clientId);
    }

    public Contract createContract(Long coachId, ContractRequest contractRequest) {

        // Get Client
        log.info("Client id:{}", contractRequest);
        User client = userRepository.findById(contractRequest.getClientId())
                .orElseThrow(() -> new UserNotFoundException("Client by id " + contractRequest.getClientId()
                + " not found"));

        // Get Coach
        Optional<User> user = userRepository.findById(coachId);
        User coach = null;
        Contract contract1 = null;
        if (user.isPresent()) {
            coach = user.get();

            // Save Contract
            Contract contract = new Contract();

            contract.setCoachingTopic(contractRequest.getCoachingTopic());
            contract.setCoachingCategory(contractRequest.getCoachingCategory());
            contract.setStartDate(contractRequest.getStartDate());
            contract.setEndDate((contractRequest.getEndDate()));
            contract.setIndividualFeesPerSession(contractRequest.getIndividualFeesPerSession());
            contract.setGroupFeesPerSession(contractRequest.getGroupFeesPerSession());
            contract.setNoOfSessions(contractRequest.getNoOfSessions());
            contract.setContractStatus(ContractStatus.NEW);
            contract.setServices(contractRequest.getServices());
            contract.setPractice(contractRequest.getPractice());
            contract.setTerms_and_conditions(contractRequest.getTerms_and_conditions());
            contract.setNote(contractRequest.getNote());
            // contract Number Generation
            int randNo = (int) ((Math.random() * (99 - 1)) + 1);
            String contractL = String.format("%05d", randNo);
            String contractNo = client.getAddedBy().getBusinessName().substring(0, 2) +
                    client.getFirstName().charAt(0) + client.getLastName().charAt(0) + "-" + contractL;
            contract.setContractNumber(contractNo);


            Float amountDue = (contractRequest.getCoachingCategory() == CoachingCategory.INDIVIDUAL) ?
                    contractRequest.getIndividualFeesPerSession() * contract.getNoOfSessions() :
                    contractRequest.getGroupFeesPerSession() * contract.getNoOfSessions();
            log.info("Amount Due:{} ", amountDue);
            contract.setAmountDue(amountDue);
            clientBillingAccountService.updateBillingAccount(amountDue, coach, client);
            log.info("Amount Due:{} ", amountDue);
            log.info("Contract: " + contract.toString());
            log.info("Client: " + client.toString());
            log.info("Coach: " + coach.toString());
            contract.setClient(client);

            contract.setCoach(coach);


            if (coach.getOrganization() != null) {
                contract.setOrganization(coach.getOrganization());
            }

            log.info("Contract: " + contract.toString());

            contract1 = contractRepository.save(contract);

            List<String> objectives = contractRequest.getObjectives();
            // save Objectives
            List<CoachingObjective> coachingObjectives = new ArrayList<>();

            for (String objective :
                    objectives) {
                CoachingObjective coachingObjective = new CoachingObjective();
                coachingObjective.setObjective(objective);
                coachingObjective.setCreatedBy(coach.getFullName());

                coachingObjective.setClient(client);
                coachingObjective.setContract(contract1);
                coachingObjective.setCoach(client.getAddedBy());

                coachingObjectives.add(coachingObjective);
            }
            contractObjectiveRepository.saveAll(coachingObjectives);

            log.info("Prep to send sms");
            Map<String, Object> replacementVariables = new HashMap<>();
            replacementVariables.put("client_name", contract1.getClient().getFullName());
            replacementVariables.put("coaching_topic", contract1.getCoachingTopic());
            replacementVariables.put("start_date", contract1.getStartDate());
            replacementVariables.put("end_date", contract1.getEndDate());
            replacementVariables.put("business_name", contract1.getClient().getAddedBy().getBusinessName());

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
            notificationServiceHTTPClient.sendEmail(client.getEmail(), "Contract Created", smsContent, false);
            log.info("Email sent");


            //create notification object and send it
            Notification notification = new Notification();
            notification.setNotificationMode(NotificationMode.SMS);
            notification.setDestinationAddress(msisdn);
            notification.setSourceAddress(sourceAddress);
            notification.setContent(smsContent);
            notification.setCoachId(client.getAddedBy().getId());
            notification.setClientId(client.getId());
            // if coach is part of an organization, set the organization id
            if (client.getOrganization() != null) {
                notification.setOrganizationId(client.getOrganization().getId());
            }
            notification.setSendReason("New Contract Created");
            notification.setContract(contract1);
            //TO DO: add logic to save notification to db

            notificationRepository.save(notification);
            log.info("Notification saved");

        }
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

    @Transactional
    public void updateContractStatusByOrganizationId(Long id, ContractStatus contractStatus, Long organizationId) {
        log.info("Changing status of contract by organization {}", id);
        Optional<Contract> contract = contractRepository.findByIdAndOrganizationId(id, organizationId);

        if (contract.isEmpty()) {
            throw new IllegalStateException("Contract doesn't exist");
        }

        Contract contract1 = contract.get();

        if (contract1.getContractStatus() == ContractStatus.SIGNED) {
            log.info("Contract {} is  is signed", id);
            throw new IllegalStateException("Contract is signed");
        } else if (contract1.getContractStatus() == ContractStatus.FINISHED) {
            log.info("Contract {} is  is finished", id);
            throw new IllegalStateException("Contract is FINISHED");

        } else if (Objects.equals(contractStatus, "SIGN")) {
            contract1.setContractStatus(ContractStatus.SIGNED);
        } else {
            contract1.setContractStatus(ContractStatus.FINISHED);
        }
        log.info("Contract status updated");
    }

    @Transactional
    public void updateContractStatusByCoachId(Long id, ContractStatus contractStatus, Long coachId) {
        log.info("Changing status of contract by coach {}", id);
        Optional<Contract> contract = contractRepository.findByIdAndCoachId(id, coachId);

        if (contract.isEmpty()) {
            throw new IllegalStateException("Contract doesn't exist");
        }

        Contract contract1 = contract.get();

        if (contract1.getContractStatus() == ContractStatus.SIGNED) {
            log.info("Contract {} is  is signed", id);
            throw new IllegalStateException("Contract is signed");
        } else if (contract1.getContractStatus() == ContractStatus.FINISHED) {
            log.info("Contract {} is  is finished", id);
            throw new IllegalStateException("Contract is FINISHED");

        } else if (Objects.equals(contractStatus, "SIGN")) {
            contract1.setContractStatus(ContractStatus.SIGNED);
        } else if (contract1.getContractStatus() == ContractStatus.NEW) {
            contract1.setContractStatus(ContractStatus.FINISHED);
        } else {
            contract1.setContractStatus(ContractStatus.FINISHED);
        }
        log.info("Contract status updated");
    }

    @Transactional
    public void updateContractStatusByClientId(Long id, ContractStatus contractStatus, Long clientId) {
        log.info("Changing status of contract by client {}", id);
        Optional<Contract> contract = contractRepository.findByIdAndClientId(id, clientId);

        if (contract.isEmpty()) {
            throw new IllegalStateException("Contract doesn't exist");
        }

        Contract contract1 = contract.get();

        if (contract1.getContractStatus() == ContractStatus.SIGNED && contractStatus == ContractStatus.FINISHED) {
            log.info("Contract {} is  is signed", id);
            contract1.setContractStatus(ContractStatus.FINISHED);
        } else if (contract1.getContractStatus() == ContractStatus.FINISHED) {
            log.info("Contract {} is  is finished", id);
            throw new IllegalStateException("Contract is FINISHED");

        } else if (contract1.getContractStatus() == null && contractStatus == ContractStatus.SIGNED) {
            contract1.setContractStatus(ContractStatus.SIGNED);
        } else if (contract1.getContractStatus() == ContractStatus.SIGNED && contractStatus == ContractStatus.SIGNED) {
            throw new IllegalStateException("Contract is signed");
        } else if (contract1.getContractStatus() == ContractStatus.NEW && contractStatus == ContractStatus.SIGNED) {
            contract1.setContractStatus(ContractStatus.SIGNED);
        } else if (contract1.getContractStatus() == ContractStatus.NEW && contractStatus == ContractStatus.FINISHED) {
            contract1.setContractStatus(ContractStatus.FINISHED);
        } else {
            throw new IllegalStateException("Contract must be signed");
        }
        log.info("Contract status updated");
    }

    public Contract createOrganizationAndCoachContract(Long organizationId, ContractRequest contractRequest) {

        // Get Coach

        User coach = userRepository.findById(contractRequest.getCoachId()).orElseThrow(() -> new UserNotFoundException("Coach by id " + contractRequest.coachId
                + " not found"));

        // Get organization
        Organization organization = organizationService.findOrganizationById(organizationId);
        // Save Contract
        Contract contract = new Contract();

        contract.setCoachingTopic(contractRequest.getCoachingTopic());
        contract.setCoachingCategory(contractRequest.getCoachingCategory());
        contract.setStartDate(contractRequest.getStartDate());
        contract.setEndDate((contractRequest.getEndDate()));
        contract.setIndividualFeesPerSession(contractRequest.getIndividualFeesPerSession());
        contract.setGroupFeesPerSession(contractRequest.getGroupFeesPerSession());
        contract.setNoOfSessions(contractRequest.getNoOfSessions());
        int randNo = (int) ((Math.random() * (99 - 1)) + 1);
        String contractL = String.format("%05d", randNo);
        String contractNo = coach.getOrganization().getOrgName().substring(0, 2) +
                coach.getFirstName().charAt(0) + coach.getLastName().charAt(0) + "-" + contractL;
        contract.setContractNumber(contractNo);
        Float amountDue = (contractRequest.getCoachingCategory() == CoachingCategory.INDIVIDUAL) ?
                contractRequest.getIndividualFeesPerSession() * contract.getNoOfSessions() :
                contractRequest.getGroupFeesPerSession() * contract.getNoOfSessions();
        log.info("Amount Due:{} ", amountDue);
        contract.setAmountDue(amountDue);
        coachBillingAccountService.updateOrganizationAndCoachBillingAccount(amountDue, organization, coach);
        log.info("Amount Due:{} ", amountDue);
        log.info("Contract: " + contract.toString());
        log.info("Coach: " + coach.toString());

        contract.setCoach(coach);
        contract.setContractNumber(contractNo);
        contract.setContractStatus(ContractStatus.NEW);
        contract.setServices(contractRequest.getServices());
        contract.setPractice(contractRequest.getPractice());
        contract.setTerms_and_conditions(contractRequest.getTerms_and_conditions());
        contract.setNote(contractRequest.getNote());

        if (coach.getOrganization() != null) {
            contract.setOrganization(coach.getOrganization());
        }


        log.info("Contract: " + contract.toString());
        //check client wallet balance
        Contract contract1 = contractRepository.save(contract);
        log.info("Contract has been saved");

        List<String> objectives = contractRequest.getObjectives();
        // save Objectives
        List<CoachingObjective> coachingObjectives = new ArrayList<>();

        for (String objective :
                objectives) {
            CoachingObjective coachingObjective = new CoachingObjective();
            coachingObjective.setObjective(objective);
            coachingObjective.setCreatedBy(organization.getOrgName());
            coachingObjective.setLastUpdatedBy(organization.getOrgName());

            coachingObjective.setCoach(coach);
            coachingObjective.setContract(contract1);
            coachingObjective.setOrganization(organization);

            coachingObjectives.add(coachingObjective);
        }
        contractObjectiveRepository.saveAll(coachingObjectives);

        log.info("Prep to send sms");
        Map<String, Object> replacementVariables = new HashMap<>();
        replacementVariables.put("coach_name", contract1.getCoach().getFullName());
        replacementVariables.put("coaching_topic", contract1.getCoachingTopic());
        replacementVariables.put("start_date", contract1.getStartDate());
        replacementVariables.put("end_date", contract1.getEndDate());
        replacementVariables.put("full_name", contract1.getCoach().getOrganization().getOrgName());

        String smsTemplate = Constants.DEFAULT_NEW_ORGANIZATION_COACH_CONTRACT_SMS_TEMPLATE;

        //replacement to get content
        String smsContent = NotificationUtil.generateContentFromTemplate(smsTemplate, replacementVariables);
        String sourceAddress = Constants.DEFAULT_SMS_SOURCE_ADDRESS; //TO-DO get this value from cooperative settings
        String referenceId = contract1.getId().toString();
        String msisdn = contract1.getCoach().getMsisdn();

        log.info("about to send message to Client content: {}, from: {}, to: {}, ref id {}", smsContent, sourceAddress, msisdn, referenceId);

        //send sms
        notificationServiceHTTPClient.sendSMS(sourceAddress, msisdn, smsContent, referenceId);
        log.info("sms sent ");

        // sendEmail
        notificationServiceHTTPClient.sendEmail(coach.getEmail(), "Contract Created", smsContent, false);
        log.info("Email sent");


        //create notification object and send it
        Notification notification = new Notification();
        notification.setNotificationMode(NotificationMode.SMS);
        notification.setDestinationAddress(msisdn);
        notification.setSourceAddress(sourceAddress);
        notification.setContent(smsContent);
        notification.setOrganizationId(organization.getId());
        notification.setCoachId(coach.getId());
        notification.setContract(contract1);
        notification.setCreatedBy(organization.getOrgName());
        //TO DO: add logic to save notification to db

        notificationRepository.save(notification);
        log.info("Notification saved");
        return contract1;
    }

    public Contract createOrganizationAndClientContract(Long organizationId, ContractRequest contractRequest) {

        log.info("Organization with id {} ", organizationId);

        // Get Client

        User client = userRepository.findById(contractRequest.getClientId()).orElseThrow(() -> new UserNotFoundException("Client by id " + contractRequest.getClientId()
                + " not found"));

        // Get Coach
        Organization organization = organizationService.findOrganizationById(organizationId);
        // Save Contract
        Contract contract = new Contract();
        contract.setCoachingTopic(contractRequest.getCoachingTopic());
        contract.setCoachingCategory(contractRequest.getCoachingCategory());
        contract.setStartDate(contractRequest.getStartDate());

        contract.setEndDate((contractRequest.getEndDate()));
        contract.setIndividualFeesPerSession(contractRequest.getIndividualFeesPerSession());
        contract.setGroupFeesPerSession(contractRequest.getGroupFeesPerSession());
        contract.setNoOfSessions(contractRequest.getNoOfSessions());
        int randNo = (int) ((Math.random() * (99 - 1)) + 1);
        String contractL = String.format("%05d", randNo);
        String contractNo = client.getOrganization().getOrgName().substring(0, 2) +
                client.getFirstName().charAt(0) + client.getLastName().charAt(0) + "-" + contractL;
        contract.setContractNumber(contractNo);
        Float amountDue = (contractRequest.getCoachingCategory() == CoachingCategory.INDIVIDUAL) ?
                contractRequest.getIndividualFeesPerSession() * contract.getNoOfSessions() :
                contractRequest.getGroupFeesPerSession() * contract.getNoOfSessions();
        log.info("Amount Due:{} ", amountDue);
        contract.setAmountDue(amountDue);
        clientBillingAccountService.updateOrganizationAndClientBillingAccount(amountDue, organization, client);
        log.info("Amount Due:{} ", amountDue);
        log.info("Contract: " + contract.toString());
        log.info("Client: " + client.toString());

        contract.setClient(client);
        contract.setContractNumber(contractNo);
        contract.setContractStatus(ContractStatus.NEW);
        contract.setServices(contractRequest.getServices());
        contract.setPractice(contractRequest.getPractice());
        contract.setTerms_and_conditions(contractRequest.getTerms_and_conditions());
        contract.setNote(contractRequest.getNote());

        if (client.getOrganization() != null) {
            contract.setOrganization(client.getOrganization());
        }


        log.info("Contract: " + contract.toString());
        //check client wallet balance
        Contract contract1 = contractRepository.save(contract);
        log.info("Contract has been saved");

        List<String> objectives = contractRequest.getObjectives();
        // save Objectives
        List<CoachingObjective> coachingObjectives = new ArrayList<>();

        for (String objective :
                objectives) {
            CoachingObjective coachingObjective = new CoachingObjective();
            coachingObjective.setObjective(objective);
            coachingObjective.setCreatedBy(organization.getOrgName());
            coachingObjective.setLastUpdatedBy(organization.getOrgName());

            coachingObjective.setClient(client);
            coachingObjective.setContract(contract1);
            coachingObjective.setOrganization(organization);

            coachingObjectives.add(coachingObjective);
        }
        contractObjectiveRepository.saveAll(coachingObjectives);
        log.info("Prep to send sms");
        Map<String, Object> replacementVariables = new HashMap<>();
        replacementVariables.put("client_name", contract1.getClient().getFullName());
        replacementVariables.put("start_date", contract1.getStartDate());
        replacementVariables.put("end_date", contract1.getEndDate());
        replacementVariables.put("org_name", contract1.getClient().getOrganization().getOrgName());

        String smsTemplate = Constants.DEFAULT_NEW_ORGANIZATION_CLIENT_CONTRACT_SMS_TEMPLATE;

        //replacement to get content
        String smsContent = NotificationUtil.generateContentFromTemplate(smsTemplate, replacementVariables);
        String sourceAddress = Constants.DEFAULT_SMS_SOURCE_ADDRESS; //TO-DO get this value from cooperative settings
        String referenceId = contract1.getId().toString();
        String msisdn = contract1.getClient().getMsisdn();
        String email = contract1.getClient().getEmail();

        log.info("about to send message to Client content: {}, from: {}, to: {}, ref id {}", smsContent, sourceAddress, msisdn, referenceId);

        //send sms
        notificationServiceHTTPClient.sendSMS(sourceAddress, msisdn, smsContent, referenceId);
        log.info("sms sent ");

        // sendEmail
        notificationServiceHTTPClient.sendEmail(client.getEmail(), "Contract Created", smsContent, false);
        log.info("Email sent");


        //create notification object and send it
        Notification notification = new Notification();
        notification.setNotificationMode(NotificationMode.SMS);
        notification.setDestinationAddress(msisdn);
        notification.setSourceAddress(sourceAddress);
        notification.setContent(smsContent);
        notification.setOrganizationId(organization.getId());
        notification.setClientId(client.getId());
        notification.setContract(contract1);
        notification.setCreatedBy(organization.getOrgName());
        //TO DO: add logic to save notification to db

        notificationRepository.save(notification);
        log.info("Notification saved");
        return contract1;


    }
}
