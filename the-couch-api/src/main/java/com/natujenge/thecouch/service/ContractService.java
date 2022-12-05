package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Contract;
import com.natujenge.thecouch.repository.ClientObjectiveRepository;
import com.natujenge.thecouch.repository.ContractRepository;
import com.natujenge.thecouch.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractService {
    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    ClientObjectiveRepository clientObjectiveRepository;

    @Autowired
    ContractRepository contractRepository;


    public List<Contract> getContracts(Long coachId) {
        return contractRepository.findAllByCoachId(coachId);
    }
}
