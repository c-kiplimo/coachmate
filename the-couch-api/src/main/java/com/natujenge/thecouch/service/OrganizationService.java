package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.repository.OrganizationRepository;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.TypeCache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Organization addNewOrganization(Organization organization){
        log.info("Add a new organization request {}", organization );

        Organization organization1 = organizationRepository.save(organization);
        return organization1;
    }

    public ListResponse getAllOrganizations(int page, int perPage) {
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<Organization> organizationPage;
        organizationPage = organizationRepository.findAll(pageable);
        return new ListResponse(organizationPage.getContent(), organizationPage.getTotalPages(),
                organizationPage.getNumberOfElements(),
                organizationPage.getTotalElements());
    }

    public Optional<Organization> findById(Long id, Long superCoachId) {
        return organizationRepository.findBySuperCoachIdAndId(superCoachId, id);
    }

    public void deleteOrganization(Long id, Long superCoachId) {
        boolean exist = organizationRepository.existsBySuperCoachIdAndId(superCoachId, id);
        if(!exist) {
            throw new IllegalStateException("Organization does not exist");
        }
        organizationRepository.deleteById(id);
    }
}