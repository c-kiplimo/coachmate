package com.natujenge.thecouch.service;

import com.natujenge.thecouch.config.Constants;
import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.*;

import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.*;
import com.natujenge.thecouch.service.dto.ContractDTO;
import com.natujenge.thecouch.service.mapper.ContractMapper;
import com.natujenge.thecouch.service.notification.NotificationServiceHTTPClient;
import com.natujenge.thecouch.util.NotificationUtil;
import com.natujenge.thecouch.web.rest.request.ContractRequest;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@Data
public class ContractService {

    private final SessionRepository sessionRepository;

    private final ContractMapper contractMapper;

    private final OrganizationService organizationService;




    private final ContractRepository contractRepository;


    private final UserService userService;


    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    private final NotificationServiceHTTPClient notificationServiceHTTPClient;

    private final NotificationSettingsService notificationSettingsService;

    private final ClientBillingAccountService clientBillingAccountService;


    private final NotificationService notificationService;




    private final CoachBillingAccountService coachBillingAccountService;

    public ContractService(SessionRepository sessionRepository, ContractMapper contractMapper, OrganizationService organizationService
                           , ContractRepository contractRepository
                           , UserService userService, NotificationRepository notificationRepository,
                           UserRepository userRepository, NotificationServiceHTTPClient notificationServiceHTTPClient,
                           NotificationSettingsService notificationSettingsService, ClientBillingAccountService clientBillingAccountService,
                           NotificationService notificationService
                           , CoachBillingAccountService coachBillingAccountService) {
        this.sessionRepository = sessionRepository;
        this.contractMapper = contractMapper;
        this.organizationService = organizationService;
        this.contractRepository = contractRepository;

        this.userService = userService;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.notificationServiceHTTPClient = notificationServiceHTTPClient;
        this.notificationSettingsService = notificationSettingsService;
        this.clientBillingAccountService = clientBillingAccountService;
        this.notificationService = notificationService;
        this.coachBillingAccountService = coachBillingAccountService;
    }


    public Contract findContractById(Long contractId) {

        // Verify Coach

        Optional<Contract> optionalContract = contractRepository.findById(contractId);

        if (optionalContract.isEmpty()) {
            throw new IllegalArgumentException("Contract with Id " + contractId + " does not exist!");
        }
        return optionalContract.get();
    }
    public Contract createContract(Long coachId, ContractRequest contractRequest,Long organizationId) {

        // Get Client
        log.info("Client id:{}", contractRequest);
        User client = userRepository.findById(contractRequest.getClientId())
                .orElseThrow(() -> new UserNotFoundException("Client by id " + contractRequest.getClientId()
                + " not found"));

        // Get Coach
        Optional<User> user = userRepository.findById(coachId);
        log.info("user --{}",user.get());
        log.info("coachid------{}",coachId);
        User coach = null;
        Contract savedcontract = null;
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
            contract.setObjective(contractRequest.getObjectives());
            if(organizationId !=null){
                contract.getOrganization().setId(organizationId);


            }
            log.info("coach------{}",coach);
            contract.setCoach(coach);


            contract.setTerms_and_conditions(contractRequest.getTerms_and_conditions());


            contract.setObjective(contractRequest.getObjectives());
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
//            clientBillingAccountService.updateBillingAccount(amountDue, coach, client);
            log.info("Amount Due:{} ", amountDue);
            log.info("Contract: " + contract.toString());
            log.info("Client: " + client.toString());
            log.info("Coach: " + coach.toString());
            contract.setClient(client);

            contract.setCoach(coach);


            if (coach.getOrganization() != null) {
                contract.setOrganization(coach.getOrganization());
            }
            contract.setCreatedBy(coachId);

            log.info("Contract: " + contract.toString());

            savedcontract = contractRepository.save(contract);



            log.info("Prep to send sms");
            Map<String, Object> replacementVariables = new HashMap<>();
            replacementVariables.put("client_name", savedcontract.getClient().getFullName());
            replacementVariables.put("coaching_topic", savedcontract.getCoachingTopic());
            replacementVariables.put("start_date", savedcontract.getStartDate());
            replacementVariables.put("end_date", savedcontract.getEndDate());
            replacementVariables.put("business_name", savedcontract.getClient().getAddedBy().getBusinessName());

            String smsTemplate = Constants.DEFAULT_NEW_CONTRACT_SMS_TEMPLATE;

            //replacement to get content
            String smsContent = NotificationUtil.generateContentFromTemplate(smsTemplate, replacementVariables);
            String sourceAddress = Constants.DEFAULT_SMS_SOURCE_ADDRESS; //TO-DO get this value from cooperative settings
            String referenceId = savedcontract.getId().toString();
            String msisdn = savedcontract.getClient().getMsisdn();

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
            notification.setContract(savedcontract);
            //TO DO: add logic to save notification to db

            notificationRepository.save(notification);
            log.info("Notification saved");

        }
        return savedcontract;
    }

    public void deleteContract(Long coachId, Long contractId) {

        // GetContract ById and CoachId
        Contract contract = findContractById(contractId);

        contractRepository.delete(contract);
    }




@Transactional
    public Contract updateContractStatus(Long contractId, ContractStatus contractStatus,Long loggedInUSerId){
        Optional<Contract> contractOptional = contractRepository.findById(contractId);
        if (contractOptional.isEmpty()) {
            throw new IllegalStateException("Contract doesn't exist");
        }
        Contract contract = contractOptional.get();

        if(contractStatus==ContractStatus.SIGNED){
            if(contract.getContractStatus()==ContractStatus.SIGNED ){
                log.info("Contract {} is  is signed", contractId);
                throw new IllegalStateException("Contract is signed");

            }else{
                contract.setContractStatus(ContractStatus.SIGNED);
                contract.setLastUpdatedBy(loggedInUSerId);

            }

        }else if(contractStatus==ContractStatus.FINISHED){
          if(contract.getContractStatus()==ContractStatus.FINISHED){
              log.info("Contract {} is  is finished", contractId);
              throw new IllegalStateException("Contract is FINISHED");
          }else{
              contract.setContractStatus(ContractStatus.FINISHED);
              contract.setLastUpdatedBy(loggedInUSerId);

          }

        }
return  contract;
    }

    private Contract createExample(Long userId,Long clientId, UserRole userRole,String search, Long organisationId) {
        Contract  contactExample = new Contract();

        User coach=new User();



        if (organisationId != null) {
            contactExample.setCoach(coach);


            log.info("org id {}", organisationId);
            contactExample.getCoach().setOrganization(new Organization());
            contactExample.getCoach().getOrganization().setId(organisationId);
        }
        if (userId != null && userRole.equals(UserRole.COACH)) {

            contactExample.setCoach(coach);

            log.info("User role is  {}", userRole);
            contactExample.getCoach().setId(userId);
            contactExample.getCoach().setUserRole(userRole);
        }

        if (clientId != null || userRole.equals(UserRole.CLIENT)) {
            User client=new User();
            log.info("User role is {}", userRole);
            contactExample.setClient(client);
            contactExample.getClient().setId(clientId);

        }





        return contactExample;
    }


    public Page<ContractDTO> filter(Long userId, Long clientId, UserRole userRole, String search, Long organisationId, Pageable pageable) {
        Contract contract = createExample(userId,clientId,userRole,search,organisationId);
        log.info("After example {} ", contract);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("coach.locked", "coach.enabled","coach.onboarded","client.locked", "client.enabled","client.onboarded")

                .withIgnoreNullValues();
        Example<Contract> example = Example.of(contract, matcher);
        return contractRepository.findAll(example, pageable).map(contractMapper::toDto);

    }
}
