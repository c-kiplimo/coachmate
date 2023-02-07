package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.SessionStatus;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.ClientRepository;
import com.natujenge.thecouch.repository.ContractObjectiveRepository;
import com.natujenge.thecouch.repository.ContractRepository;
import com.natujenge.thecouch.repository.SessionRepository;
import com.natujenge.thecouch.web.rest.request.ContractRequest;
import com.natujenge.thecouch.web.rest.request.SessionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
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
    ContractObjectiveRepository contractObjectiveRepository;

    public List<Contract> getContracts(Long coachId) {
        return contractRepository.findAllByCoachId(coachId);
    }

    public Contract getSingleContract(Long coachId, Long contractId) {

        // Verify Coach

        Optional<Contract> optionalContract = contractRepository.findByIdAndCoachId(contractId,coachId);

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
        Client client = clientRepository.findByIdAndCoachId(contractRequest.getClientId(),
                coachId).orElseThrow(() -> new UserNotFoundException("Client by id " + contractRequest.getClientId()
                + " not found"));

        // Get Coach
        Coach coach = coachService.findCoachById(coachId);
        // Save Contract
        Contract contract = new Contract();

        contract.setCoachingTopic(contractRequest.getCoachingTopic());
        contract.setCoachingCategory(contractRequest.getCoachingCategory());
        contract.setStartDate(contractRequest.getStartDate());
        contract.setEndDate((contractRequest.getEndDate()));
        //contract.setFeesPerPerson(contractRequest.getFeesPerPerson());
        contract.setIndividualFeesPerSession(contractRequest.getIndividualFeesPerSession());
        contract.setGroupFeesPerSession(contractRequest.getGroupFeesPerSession());
        contract.setNoOfSessions(contractRequest.getNoOfSessions());
        contract.setClient(client);
        contract.setCoach(coach);
        contract.setOrgId(coach.getOrgIdId());

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

        // Save Sessions
       // List<SessionRequest> sessionRequests = contractRequest.getSessions();

        // save Objectives
        //List<Session> coachingSessions = new ArrayList<>();

//        for (SessionRequest sessionRequest:
//                sessionRequests) {
//            Session session = new Session();
//            session.setName(sessionRequest.getName());
//            session.setSessionType(sessionRequest.getSessionType());
//            session.setSessionStatus(SessionStatus.CONFIRMED);
//            session.setNotes(sessionRequest.getNotes());
//            session.setSessionDate(sessionRequest.getSessionDate());
//            session.setSessionDuration(sessionRequest.getSessionDuration());
//            session.setSessionVenue(sessionRequest.getSessionVenue());
//            session.setPaymentCurrency(sessionRequest.getPaymentCurrency());
//            session.setAmountPaid(sessionRequest.getAmountPaid());
//            session.setCreatedBy(coach.getFullName());
//            session.setLastUpdatedBy(coach.getFullName());

            // Relations
//            session.setClient(client);
//            session.setContract(contract1);
//            session.setCoach(coach);

//            coachingSessions.add(session);
//        }
//        sessionRepository.saveAll(coachingSessions);

        return contract1;
    }

    public void deleteContract(Long coachId, Long contractId) {

        // GetContract ById and CoachId
        Contract contract = getSingleContract(coachId,contractId);

        contractRepository.delete(contract);
    }


    public List<Contract> getContractByOrgId(Long orgId) {
        return contractRepository.findContractByOrgId(orgId);
    }
}
