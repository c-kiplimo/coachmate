package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.ClientStatus;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.ClientRepository;
import com.natujenge.thecouch.repository.ClientWalletRepository;
import com.natujenge.thecouch.repository.CoachRepository;
import com.natujenge.thecouch.repository.OrganizationRepository;
import com.natujenge.thecouch.repository.UserRepository;
import com.natujenge.thecouch.web.rest.dto.ClientDto;
import com.natujenge.thecouch.web.rest.dto.ListResponse;
import com.natujenge.thecouch.web.rest.request.ChangeStatusRequest;
import com.natujenge.thecouch.web.rest.request.ClientRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import static org.springframework.util.StringUtils.hasLength;
import com.natujenge.thecouch.domain.AccountStatement;
@Service
@Slf4j
public class ClientService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CoachRepository coachRepository;

    @Autowired

    ClientBillingAccountService clientBillingAccountService;
    @Autowired
    AccountStatementService accountStatementService;

    @Autowired
    OrganizationRepository organizationRepository;


    private final ClientRepository clientRepository;
    private final RegistrationService registrationService;
    private final UserRepository userRepository;
    private final ClientWalletRepository clientWalletRepository;
    public ClientService(ClientRepository clientRepository, RegistrationService registrationService,
                         UserRepository userRepository, ClientWalletRepository clientWalletRepository) {
        this.clientRepository = clientRepository;
        this.registrationService = registrationService;
        this.userRepository = userRepository;
        this.clientWalletRepository = clientWalletRepository;
    }



    // create client
    public Client addNewClient(Long coachId, ClientRequest clientRequest) {
        log.info("add a new client to database");

        Optional<Coach> optionalCoach = coachRepository.getCoachById(coachId);

        if (optionalCoach.isEmpty()) {
            log.warn("Coach with id {} not found", coachId);
            throw new IllegalArgumentException("Coach not found");

        }

        Optional<Organization> optionalOrganization = organizationRepository.getOrganizationBySuperCoachId(coachId);


        Client client = new Client();

        if (optionalOrganization.isPresent()){
            client.setOrganization(optionalOrganization.get());
        }

        client.setCoach(optionalCoach.get());

        client.setFirstName(clientRequest.getFirstName());
        client.setLastName(clientRequest.getLastName());
        client.setFullName(clientRequest.getFirstName()+' '+clientRequest.getLastName());
        client.setClientType(clientRequest.getClientType());
        client.setMsisdn(clientRequest.getMsisdn());
        client.setEmail(clientRequest.getEmail());
        client.setPhysicalAddress(clientRequest.getPhysicalAddress());
        client.setStatus(ClientStatus.ACTIVE);
        client.setReason(clientRequest.getReason());
        client.setPaymentMode(clientRequest.getPaymentMode());
        client.setCreatedAt(LocalDateTime.now());
        client.setCreatedBy(optionalCoach.get().getFullName());
        client.setProfession(clientRequest.getProfession());


        Client saveClient = clientRepository.save(client);

        registrationService.registerClientAsUser(saveClient);

        // Create client wallet
        ClientWallet clientWallet = new ClientWallet();
        clientWallet.setCreatedBy(optionalCoach.get().getFullName());
        clientWallet.setClient(saveClient);
        clientWallet.setCoach(optionalCoach.get());
        clientWallet.setWalletBalance(Float.valueOf(0));
        optionalOrganization.ifPresent(clientWallet::setOrganization);
        clientWalletRepository.save(clientWallet);
        log.info("Client Wallet created Successfully!");

        // Create client Billing Account
        ClientBillingAccount clientBillingAccount = new ClientBillingAccount();
        clientBillingAccount.setCreatedBy(optionalCoach.get().getFullName());
        clientBillingAccount.setCoach(optionalCoach.get());
        optionalOrganization.ifPresent(clientBillingAccount::setOrganization);
        clientBillingAccount.setClient(saveClient);
        clientBillingAccount.setAmountBilled((float) 0);
        clientBillingAccountService.createBillingAccount(clientBillingAccount);
        log.info("Client Billing Account created Successfully!");
        return saveClient;

    }

    // Get all clients
    public ListResponse getAllClients(int page, int perPage, String clientName,
                                      ClientStatus clientStatus,
                                        Long coachId) {

        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientDto> clientPage;
        QClient qClient = QClient.client;
        // search by name or email or phone containing search param
        if (clientName != null && !clientName.isEmpty()) {
            clientPage = clientRepository.findBy(qClient.coach.id.eq(coachId).
                            andAnyOf(qClient.fullName.containsIgnoreCase(clientName).
                                    or(qClient.msisdn.containsIgnoreCase(clientName).
                                            or(qClient.email.containsIgnoreCase(clientName)))),
                    q -> q.sortBy(sort).as(ClientDto.class).page(pageable));

            // Status Only
        } else if (clientStatus != null && clientName == null) {
            clientPage = clientRepository.findBy(qClient.coach.id.eq(coachId).
                            and(qClient.status.eq(clientStatus)),
                    q -> q.sortBy(sort).as(ClientDto.class).page(pageable));


            // Status plus Name > ClientName always true
        } else if (clientStatus != null) {
            clientPage = clientRepository.findBy(qClient.coach.id.eq(coachId).
                            and(qClient.status.eq(clientStatus).andAnyOf(
                                    qClient.fullName.containsIgnoreCase(clientName).
                                            or(qClient.msisdn.containsIgnoreCase(clientName).
                                                    or(qClient.email.containsIgnoreCase(clientName)))
                            )),
                    q -> q.sortBy(sort).as(ClientDto.class).page(pageable));


            // Get All clients
        } else {
            clientPage = clientRepository.findAllByCoach_id(coachId, pageable);
        }

        return new ListResponse(clientPage.getContent(), clientPage.getTotalPages(),
                clientPage.getNumberOfElements(),
                clientPage.getTotalElements());
    }

    //get one client
    public Optional<Client> findById(Long id) {

        return clientRepository.findById(id);
    }

    //Update client
    @Transactional
    public Optional<Client> updateClient(Long id,Long coachId,
                                         ClientRequest clientRequest) {
        Optional<Client> clientOptional = clientRepository.findByIdAndCoachId(id,coachId);
        log.info("Client found with id {}", id);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();

            client.setFirstName(clientRequest.getFirstName());
            client.setLastName(clientRequest.getLastName());
            client.setEmail(clientRequest.getEmail());
            client.setMsisdn(clientRequest.getMsisdn());
            client.setPhysicalAddress(clientRequest.getPhysicalAddress());
            client.setReason(clientRequest.getReason());
            client.setPaymentMode(clientRequest.getPaymentMode());
            client.setProfession(clientRequest.getProfession());
            client.setClientType(clientRequest.getClientType());

            client = clientRepository.save(client);
            log.info("Updated client with id {}:", client.getId());
            return Optional.of(client);
        } else {
            log.error("Client not found with id {}", id);
            return Optional.empty();
        }
    }

    //search by name
    public List<Client> search(String text) {
        log.debug("Request to search client with text : {}", text);

        return clientRepository.findByFullNameContaining(text);
    }

    //get client by name and coachid
    @Transactional(readOnly = true)
    public ListResponse filterByNameAndCoachId(
            int page,
            int perPage,
            Long coachId,
            String name
    ) {
        log.debug(
                "Request to filter clients given coachId : {}, client name : {}",
                coachId,
                name

        );
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientDto> clientPage =  clientRepository.findByCoachIdAndFullNameContaining(
                coachId,
                name,
                pageable
        );
        return new ListResponse(clientPage.getContent(), clientPage.getTotalPages(),
                clientPage.getNumberOfElements(),
                clientPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ListResponse filterByIdAndCoachId(int page, int perPage, Long coachId, Long id) {
        log.debug(
                "Request to filter clients given coachId : {}, clientr id : {}",
                coachId,
                id

        );
        page = page - 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, perPage, sort);

        Page<ClientDto> clientPage =  clientRepository.findByCoachIdAndId(
                coachId,
                id,
                pageable
        );
        return new ListResponse(clientPage.getContent(), clientPage.getTotalPages(),
                clientPage.getNumberOfElements(),
                clientPage.getTotalElements());
    }
    @Transactional
    public void updateClientStatus(Long id, Long coachId, ClientStatus clientStatus,
                                     ChangeStatusRequest statusRequest) {
        log.info("Changing status of client {}",id);
        Optional<Client> client = clientRepository.findByIdAndCoachId(id,coachId);

        if (client.isEmpty()){
            throw new IllegalStateException("Client doesn't exist");
        }

        Client client1 = client.get();

        if (client1.getStatus() == ClientStatus.CLOSED){
            log.info("Client {} is in CLOSED state",id);
            throw new IllegalStateException("Client is in CLOSED state");
        }

        else if (Objects.equals(clientStatus, ClientStatus.ACTIVE)){
            client1.setStatus(ClientStatus.ACTIVE);
            client1.setReason(statusRequest.getNarration());
            client1.setLastUpdatedAt(LocalDateTime.now());
            client1.setLastUpdatedBy( client1.getCoach().getFullName());
        } else if (Objects.equals(clientStatus, ClientStatus.CLOSED)){
            client1.setStatus(ClientStatus.CLOSED);
            client1.setReason(statusRequest.getNarration());
            client1.setLastUpdatedAt(LocalDateTime.now());
            client1.setLastUpdatedBy( client1.getCoach().getFullName());
        } else{
            client1.setStatus(ClientStatus.SUSPENDED);
            client1.setReason(statusRequest.getNarration());
            client1.setLastUpdatedAt(LocalDateTime.now());
            client1.setLastUpdatedBy( client1.getCoach().getFullName());
        }
        log.info("Client with id {} changed status",id);
    }

    @Transactional
    public void closeClient(Long id, Long coachId,ChangeStatusRequest statusRequest) {
        log.info("Changing status of client {} to CLOSED",id);
        Optional<Client> client = clientRepository.findByIdAndCoachId(id,coachId);


        if (client.isEmpty()){
            throw new IllegalStateException("Client doesn't exist");
        }


        Client client1 = client.get();
        client1.setStatus(ClientStatus.CLOSED);
        client1.setReason(statusRequest.getNarration());
        client1.setLastUpdatedAt(LocalDateTime.now());
        client1.setLastUpdatedBy( client1.getCoach().getFullName());
        log.info("Client with id {} changed status",id);
    }


    // Delete Client
    public void deleteClient(Long id, Long coachId) {
        boolean exist = clientRepository.existsByIdAndCoachId(id,coachId);
        if (!exist) {
            throw new IllegalStateException("Client doesn't exist");

        }
        clientRepository.deleteById(id);
    }


    // Patch Client
    public void patchClient(Long id,Long coachId, ClientRequest clientRequest) {
        Client client = clientRepository.findByIdAndCoachId(id,coachId).orElseThrow(()
                -> new UserNotFoundException("Client by id " + id + " not found"));

        boolean needUpdate = false;

        if (hasLength(clientRequest.getFirstName())){
            client.setFirstName(clientRequest.getFirstName());
            needUpdate = true;
        }

        if (hasLength(clientRequest.getLastName())){
            client.setLastName(clientRequest.getLastName());
            needUpdate = true;
        }

        if (hasLength(clientRequest.getEmail())){
            client.setEmail(clientRequest.getEmail());
            needUpdate = true;
        }

        if (hasLength(clientRequest.getProfession())){
            client.setProfession(clientRequest.getProfession());
            needUpdate = true;
        }

        if (hasLength(clientRequest.getMsisdn())){
            client.setMsisdn(clientRequest.getMsisdn());
            needUpdate = true;
        }

        if (needUpdate) {
            clientRepository.save(client);
        }
    }


    public Optional<User> confirmClientTokenAndUpdatePassword(ClientRequest clientRequest) {

        Optional<User> userOptional = userRepository.findById(clientRequest.getId());
        if (userOptional.isEmpty()) {
            throw new IllegalStateException("Client User Not Found!!");
        }

        String TokenConfirm = registrationService.confirmToken(clientRequest.getToken());
        if (!TokenConfirm.isEmpty()) {
            User user = userOptional.get();

            //Encode Password
            String encodedPassword = passwordEncoder.encode(clientRequest.getPassword());
            user.setPassword(encodedPassword);

            user = userRepository.save(user);

            log.info("Password Updated Successfully");
            return Optional.of(user);
        }
//        return "Token Not Confirmed";
        return userOptional;
    }

    public List<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }


    public List<Client> getClientByOrgId(Long orgId) {

        Optional<Organization> optionalOrganization = organizationRepository.getOrganizationBySuperCoachId(orgId);

        return clientRepository.findClientByOrganization(optionalOrganization.get());
    }


}