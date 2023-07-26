package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    Page<Contract> findAllByCoachId(Long coachId, Pageable pageable);


    Optional<Contract> findByIdAndCoachId(Long contractId, Long coachId);


    List<Contract> findAllByClientId(Long clientId);

    List<Contract> findContractByOrganizationId(Long orgId);

    Optional<Contract> findByIdAndOrganizationId(Long id, Long organizationId);

    Optional<Contract> findByIdAndClientId(Long id, Long clientId);

}
