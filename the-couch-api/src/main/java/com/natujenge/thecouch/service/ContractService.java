package com.natujenge.thecouch.service;

import com.natujenge.thecouch.repository.ClientCoachingScheduleRepository;
import com.natujenge.thecouch.repository.ClientObjectiveRepository;
import com.natujenge.thecouch.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractService {
    @Autowired
    ClientCoachingScheduleRepository clientCoachingScheduleRepository;

    @Autowired
    ClientObjectiveRepository clientObjectiveRepository;

    @Autowired
    ContractRepository contractRepository;


}
