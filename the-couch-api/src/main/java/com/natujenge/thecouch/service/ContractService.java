package com.natujenge.thecouch.service;

import com.natujenge.thecouch.config.Constants;
import com.natujenge.thecouch.domain.Contract;
import com.natujenge.thecouch.domain.Notification;
import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.CoachingCategory;
import com.natujenge.thecouch.domain.enums.ContractStatus;
import com.natujenge.thecouch.domain.enums.NotificationMode;
import com.natujenge.thecouch.domain.enums.UserRole;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.ContractRepository;
import com.natujenge.thecouch.repository.NotificationRepository;
import com.natujenge.thecouch.repository.SessionRepository;
import com.natujenge.thecouch.repository.UserRepository;
import com.natujenge.thecouch.service.mapper.ContractMapper;
import com.natujenge.thecouch.service.notification.NotificationServiceHTTPClient;
import com.natujenge.thecouch.util.NotificationUtil;
import com.natujenge.thecouch.web.rest.dto.ContractDTO;
import com.natujenge.thecouch.web.rest.request.ContractRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public ContractService(SessionRepository sessionRepository, ContractMapper contractMapper,
                           OrganizationService organizationService, ContractRepository contractRepository, UserService userService,
                           NotificationRepository notificationRepository,
                           UserRepository userRepository, NotificationServiceHTTPClient notificationServiceHTTPClient,
                           NotificationSettingsService notificationSettingsService,
                           ClientBillingAccountService clientBillingAccountService,
                           NotificationService notificationService, CoachBillingAccountService coachBillingAccountService) {
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

    public Contract createContract(Long coachId, ContractRequest contractRequest, Long organizationId) {

        // Get Client
        log.info("Client id:{}", contractRequest);
        User client = userRepository.findById(contractRequest.getClientId())
                .orElseThrow(() -> new UserNotFoundException("Client by id " + contractRequest.getClientId()
                        + " not found"));

        // Get Coach
        Optional<User> user = userRepository.findById(coachId);
        log.info("user --{}", user.get());
        log.info("coachid------{}", coachId);
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



            if (organizationId != null) {
//                assert contract.getOrganization() != null;
//                contract.getOrganization().setId(organizationId);
                contract.setOrganization(organizationService.findOrganizationById(organizationId));
            }


            log.info("coach------{}", coach);
            contract.setCoach(coach);
            contract.setClient(client);

            contract.setTerms_and_conditions(contractRequest.getTerms_and_conditions());

            contract.setObjective(contractRequest.getObjectives());
            contract.setNote(contractRequest.getNote());
            // contract Number Generation
            int randNo = (int) ((Math.random() * (99 - 1)) + 1);
            String contractL = String.format("%05d", randNo);
            String contractNo = client.getAddedBy().getBusinessName().substring(0, 2) +
                    client.getFirstName().charAt(0) + client.getLastName().charAt(0) + "-" + contractL;
            contract.setContractNumber(contractNo);

            Float amountDue = (contractRequest.getCoachingCategory() == CoachingCategory.INDIVIDUAL)
                    ? contractRequest.getIndividualFeesPerSession() * contract.getNoOfSessions()
                    : contractRequest.getGroupFeesPerSession() * contract.getNoOfSessions();
            log.info("Amount Due:{} ", amountDue);
            contract.setAmountDue(amountDue);
            // clientBillingAccountService.updateBillingAccount(amountDue, coach, client);
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

            // replacement to get content
            String smsContent = NotificationUtil.generateContentFromTemplate(smsTemplate, replacementVariables);
            String sourceAddress = Constants.DEFAULT_SMS_SOURCE_ADDRESS; // TO-DO get this value from cooperative
            // settings
            String referenceId = savedcontract.getId().toString();
            String msisdn = savedcontract.getClient().getMsisdn();
            String email=savedcontract.getClient().getEmail();

            log.info("about to send message to Client content: {}, from: {}, to: {}, ref id {}", smsContent,
                    sourceAddress, msisdn, referenceId);

            // send sms
            notificationServiceHTTPClient.sendSMS(sourceAddress, msisdn, smsContent, referenceId);
            log.info("sms sent ");

            // sendEmail
            notificationServiceHTTPClient.sendEmail(email, "Contract Created", smsContent, false);
            log.info("Email sent");

            // create notification object and send it
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
            // TODO: add logic to save notification to db
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
    public Contract updateContractStatus(Long contractId, ContractStatus contractStatus, Long loggedInUSerId, UserRole userRole) {
        Optional<Contract> contractOptional = contractRepository.findById(contractId);
        if (contractOptional.isEmpty()) {
            throw new IllegalStateException("Contract doesn't exist");
        }
        Contract contract = contractOptional.get();

        if (contractStatus == ContractStatus.SIGNED) {
            if (contract.getContractStatus() == ContractStatus.SIGNED) {
                log.info("Contract {} is  is signed", contractId);
                throw new IllegalStateException("Contract is signed");

            } else {
                    if ((contract.getCoachSigned() != null && contract.getClientSigned() != null)
                    || (contract.getCoachSigned() != null && contract.getOrganizationSigned() != null)
                    || (contract.getClientSigned() != null && contract.getOrganizationSigned() != null)) {
                        contract.setContractStatus(ContractStatus.SIGNED);
                        contract.setLastUpdatedBy(loggedInUSerId);
                    } else {
                        if (userRole == UserRole.CLIENT) {
                            contract.setClientSigned(true);
                            contract.setLastUpdatedBy(loggedInUSerId);
                        } else if (userRole == UserRole.COACH) {
                            contract.setCoachSigned(true);
                            contract.setLastUpdatedBy(loggedInUSerId);
                        } else if (userRole == UserRole.ORGANIZATION) {
                            contract.setOrganizationSigned(true);
                            contract.setLastUpdatedBy(loggedInUSerId);
                        }
                    }
            }
        } else if (contractStatus == ContractStatus.ONGOING) {
            if (!(contract.getCoachSigned() && contract.getClientSigned())) {
                log.info("Contract {} is  is not signed", contractId);
                throw new IllegalStateException("Contract is not signed");
            } else {
                if (contract.getContractStatus() == ContractStatus.ONGOING) {
                    log.info("Contract {} is  is ongoing", contractId);
                    throw new IllegalStateException("Contract is ONGOING");
                } else {
                    contract.setContractStatus(ContractStatus.ONGOING);
                    contract.setLastUpdatedBy(loggedInUSerId);
                }
            }
        } else if (contractStatus == ContractStatus.FINISHED) {
            if (contract.getContractStatus() == ContractStatus.FINISHED) {
                log.info("Contract {} is  is finished", contractId);
                throw new IllegalStateException("Contract is FINISHED");
            } else {
                contract.setContractStatus(ContractStatus.FINISHED);
                contract.setLastUpdatedBy(loggedInUSerId);
            }
        }
        return contract;
    }

    private Contract createExample(Long coachId, Long clientId, String search, ContractStatus status, Long organisationId) {
        Contract contactExample = new Contract();
        User coach = new User();
        User client = new User();
        Organization organization = new Organization();

        if (organisationId != null) {
            contactExample.setOrganization(organization);
            assert contactExample.getOrganization() != null;
            contactExample.getOrganization().setId(organisationId);
        }
        if (coachId != null) {
            contactExample.setCoach(coach);
            contactExample.getCoach().setId(coachId);
        }
        if (search != null) {
            contactExample.setClient(client);
            contactExample.getClient().setFullName(search);
        }
        if (status != null) {
            contactExample.setContractStatus(status);
        }
        if (clientId != null) {
            contactExample.setClient(client);
            contactExample.getClient().setId(clientId);
        }
        return contactExample;
    }

    public Page<ContractDTO> filter(Long coachId, Long clientId, String search, ContractStatus status, Long organisationId,
                                    Pageable pageable) {
        Contract contract = createExample(coachId, clientId, search, status, organisationId);

        log.info("After example {} ", contract);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("coach.locked", "coach.enabled", "coach.onboarded", "client.locked", "client.enabled", "client.onboarded")
                .withIgnoreNullValues();

        Example<Contract> example = Example.of(contract, matcher);

        return contractRepository.findAll(example, pageable).map(contractMapper::toDto);

    }

    public ContractDTO updateContract(Long contractId, ContractRequest contractRequest) {
        Optional<Contract> contractOptional = contractRepository.findById(contractId);
        if (contractOptional.isEmpty()) {
            throw new IllegalStateException("Contract doesn't exist");
        }

        log.info("Contract {} with id {} found", contractId, contractOptional.get());
        Contract contract = contractOptional.get();

        if (contractRequest.getCoachingTopic() != null && !contractRequest.getCoachingTopic().isEmpty()) {
            contract.setCoachingTopic(contractRequest.getCoachingTopic());
        }
        if (contractRequest.getStartDate() != null) {
            contract.setStartDate(contractRequest.getStartDate());
        }
        if (contractRequest.getEndDate() != null) {
            contract.setEndDate(contractRequest.getEndDate());
        }
        if (contractRequest.getCoachingCategory() != null) {
            contract.setCoachingCategory(contractRequest.getCoachingCategory());
        }
        if (contractRequest.getNoOfSessions() > 0) {
            contract.setNoOfSessions(contractRequest.getNoOfSessions());
        }
        if (contractRequest.getTerms_and_conditions() != null && !contractRequest.getTerms_and_conditions().isEmpty()) {
            contract.setTerms_and_conditions(contractRequest.getTerms_and_conditions());
        }
        if (contractRequest.getIndividualFeesPerSession() > 0) {
            contract.setIndividualFeesPerSession(contractRequest.getIndividualFeesPerSession());
        }
        if (contractRequest.getGroupFeesPerSession() > 0) {
            contract.setGroupFeesPerSession(contractRequest.getGroupFeesPerSession());
        }

        contract = contractRepository.save(contract);

        log.info("Contract {} updated", contract);
        return contractMapper.toDto(contract);
    }

    public void checkAndUpdateContractStatus() {
        contractRepository.findAll().forEach(contract -> {
            if ((contract.getCoachSigned() != null && contract.getClientSigned() != null)
                    || (contract.getCoachSigned() != null && contract.getOrganizationSigned() != null)
                    || (contract.getClientSigned() != null && contract.getOrganizationSigned() != null) &&
                    (contract.getContractStatus() != ContractStatus.SIGNED)) {
                contract.setContractStatus(ContractStatus.SIGNED);
                contractRepository.save(contract);
            }
        });
    }
}
