package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.CoachingCategory;
import com.natujenge.thecouch.domain.enums.PaymentStatus;
import com.natujenge.thecouch.domain.enums.SessionStatus;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.ClientRepository;
import com.natujenge.thecouch.repository.ContractObjectiveRepository;
import com.natujenge.thecouch.repository.ContractRepository;
import com.natujenge.thecouch.repository.SessionRepository;
import com.natujenge.thecouch.web.rest.request.ContractRequest;
import com.natujenge.thecouch.web.rest.request.SessionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ContractService {
    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    ContractObjectiveRepository clientObjectiveRepository;

    @Autowired
    ContractRepository contractRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CoachService coachService;

    @Autowired
    ClientBillingAccountService clientBillingAccountService;

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




        float amountDue = (contractRequest.getCoachingCategory() == CoachingCategory.INDIVIDUAL)?
                contractRequest.getIndividualFeesPerSession() * contract.getNoOfSessions():
                contractRequest.getGroupFeesPerSession() * contract.getNoOfSessions();
    log.info("Amount Due:{} ",amountDue);

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
