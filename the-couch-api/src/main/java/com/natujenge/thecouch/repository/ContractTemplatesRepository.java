package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.ContractTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractTemplatesRepository extends JpaRepository<ContractTemplate, Long> {

}

