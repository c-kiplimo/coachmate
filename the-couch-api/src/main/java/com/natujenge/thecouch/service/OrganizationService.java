package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.OrganizationBillingAccount;
import com.natujenge.thecouch.domain.OrganizationWallet;
import com.natujenge.thecouch.repository.OrganizationRepository;
import com.natujenge.thecouch.repository.OrganizationWalletRepository;
import com.natujenge.thecouch.repository.UserRepository;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import lombok.extern.slf4j.Slf4j;
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

    private final UserRepository userRepository;
    private final OrganizationWalletRepository organizationWalletRepository;

    private final OrganizationBillingAccountService organizationBillingAccountService;

    public OrganizationService(OrganizationRepository organizationRepository, UserRepository userRepository, OrganizationWalletRepository organizationWalletRepository, OrganizationBillingAccountService organizationBillingAccountService) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.organizationWalletRepository = organizationWalletRepository;
        this.organizationBillingAccountService = organizationBillingAccountService;
    }

    public Organization addNewOrganization(Organization organization){
        log.info("Add a new organization request {}", organization );


        Organization savedOrganization = organizationRepository.save(organization);


        // Create client wallet
        OrganizationWallet organizationWallet = new OrganizationWallet();

        organizationWallet.setOrganization(organization);
        organizationWallet.setWalletBalance(Float.valueOf(0));
        organizationWallet.setCreatedBy(organization.getOrgName());
        organizationWalletRepository.save(organizationWallet);
        log.info("Organization Wallet created Successfully!");

        // Create client Billing Account
        OrganizationBillingAccount organizationBillingAccount = new OrganizationBillingAccount();

        organizationBillingAccount.setAmountBilled((float) 0);
        organizationBillingAccount.setCreatedBy(organization.getOrgName());
        organizationBillingAccountService.createBillingAccount(organizationBillingAccount);
        log.info("Organiation Billing Account created Successfully!");
        return savedOrganization;


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

    public Optional<Organization> getOrganizationBySuperCoachId(Long superCoachId) {
    return organizationRepository.findBySuperCoachId(superCoachId);

    }

    public Organization findOrganizationById(Long id) {
        Organization organization = organizationRepository.findOrganizationById(id);
        if (organization == null) {
            throw new IllegalArgumentException("Organization not found");
        }
        return organization;
    }

}