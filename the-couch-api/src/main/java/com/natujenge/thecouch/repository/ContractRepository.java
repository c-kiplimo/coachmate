package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findAllByCoachId(Long coachId);


    Optional<Contract> findByIdAndCoachId(Long contractId, Long coachId);
}
