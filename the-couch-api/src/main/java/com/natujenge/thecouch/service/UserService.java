package com.natujenge.thecouch.service;
import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.*;
import com.natujenge.thecouch.exception.UserNotFoundException;
import com.natujenge.thecouch.repository.ClientWalletRepository;
import com.natujenge.thecouch.repository.ContractTemplatesRepository;
import com.natujenge.thecouch.repository.OrganizationRepository;
import com.natujenge.thecouch.repository.UserRepository;
import com.natujenge.thecouch.security.SecurityUtils;
import com.natujenge.thecouch.service.mapper.ClientMapper;
import com.natujenge.thecouch.service.mapper.CoachMapper;
import com.natujenge.thecouch.service.mapper.OrganizationMapper;
import com.natujenge.thecouch.service.mapper.UserMapper;
import com.natujenge.thecouch.service.notification.NotificationServiceHTTPClient;
import com.natujenge.thecouch.util.OnBoardCoachUtil;
import com.natujenge.thecouch.web.rest.dto.*;
import com.natujenge.thecouch.web.rest.request.ClientRequest;
import com.natujenge.thecouch.web.rest.request.ContractTemplatesRequest;
import com.natujenge.thecouch.web.rest.request.UserTokenConfirmRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    private final OrganizationRepository organizationRepository;
    private final static String USER_NOT_FOUND_MSG = "User with Email %s not found!";
    private final static String USER_EXISTS = "Email %s Taken!";
    private final CoachMapper coachMapper;
    private final OrganizationMapper organizationMapper;
    private final UserRepository userRepository;
    private final RegistrationService registrationService;
    private final ClientWalletRepository clientWalletRepository;
    private final ClientBillingAccountService clientBillingAccountService;
    private final PaymentDetailsService paymentDetailsService;
    private final CoachSettingsService coachSettingsService;
    private final NotificationSettingsService notificationSettingsService;
    private final ContractTemplatesRepository contractTemplatesRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    private final UserMapper userMapper;
    private final ClientMapper clientMapper;

    public UserService(CoachMapper coachMapper, UserRepository userRepository, @Lazy RegistrationService registrationService, ClientWalletRepository clientWalletRepository, ClientBillingAccountService clientBillingAccountService, PaymentDetailsService paymentDetailsService, CoachSettingsService coachSettingsService, NotificationSettingsService notificationSettingsService, ContractTemplatesRepository contractTemplatesRepository, PasswordEncoder passwordEncoder, ConfirmationTokenService confirmationTokenService, UserMapper userMapper, ClientMapper clientMapper,
                       OrganizationRepository organizationRepository, OrganizationMapper organizationMapper) {
        this.coachMapper = coachMapper;
        this.userRepository = userRepository;
        this.registrationService = registrationService;
        this.clientWalletRepository = clientWalletRepository;
        this.clientBillingAccountService = clientBillingAccountService;
        this.paymentDetailsService = paymentDetailsService;
        this.coachSettingsService = coachSettingsService;
        this.notificationSettingsService = notificationSettingsService;
        this.contractTemplatesRepository = contractTemplatesRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.userMapper = userMapper;
        this.clientMapper = clientMapper;
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
    }

    public User enableAppUser(String email) {

        // Request UserDto rather than all details
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));

        user.setEnabled(true);
        userRepository.save(user);
        return user;

    }


    public void confirmCoach(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        // Set details
        user.setPassword(encodedPassword);
        enableAppUser(user.getMsisdn());
        return;
    }


    //Client as User
    public List<Object> signupClientAsUser(User user) {
        log.info("Signing up User");
        boolean userEmailExists = userRepository.findByEmail(user.getEmail())
                .isPresent();

        if (userEmailExists) {
            throw new IllegalStateException(String.format(USER_EXISTS, user.getEmail()));
        }

        // Encode Password > from spring boot
        //String encodedPassword = passwordEncoder.encode(user.getPassword());

        // Set details
        user.setPassword(user.getPassword());
        user.setContentStatus(ContentStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy("Test");


        // save the User in the database
        User user1 = userRepository.save(user);
        log.info("Client User saved");

        // Generate a Random 6 digit OTP - 0 - 999999
        int randomOTP = (int) ((Math.random() * (999999 - 1)) + 1);
        String token = String.format("%06d", randomOTP);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(60 * 48), // expires after 2 days of generation
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        log.info("Confirmation token generated");

        List<Object> response = new ArrayList<>();
        response.add(user1.getId());
        response.add(token);

        return response;

    }


    // Coach as user
    public List<Object> signupCoachAsUser(User user) {
        log.info("Signing up coach as user");
        boolean userEmailExists = userRepository.findByEmail(user.getEmail())
                .isPresent();

        if (userEmailExists) {
            throw new IllegalStateException(String.format(USER_EXISTS, user.getEmail()));
        }

        // Set details
        user.setContentStatus(ContentStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy(user.getMsisdn());


        // save the User in the database
        User user1 = userRepository.save(user);
        log.info("Coach User saved");

        // Generate a Random 6 digit OTP - 0 - 999999
        int randomOTP = (int) ((Math.random() * (999999 - 1)) + 1);
        String token = String.format("%06d", randomOTP);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(60 * 48),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        log.info("Confirmation token generated");

        List<Object> response = new ArrayList<>();
        response.add(user1.getId());
        response.add(token);

        return response;

    }

    public void updateUser(User registeredUser) {
        log.info("Request to update user : {}", registeredUser.getFirstName());
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findUserByUsername(registeredUser.getUsername()));

        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("User with username " + registeredUser.getUsername() + " not found!");
        }

        User user = optionalUser.get();
        user.setFirstName(registeredUser.getFirstName());

        userRepository.save(user);
        log.info("User Updated Successfully");
        return;
    }

    public Optional<User> findByEmail(String email) {
        log.info("Request to find user with email : {}", email);

        Optional<User> user = userRepository.findByEmail(email);
        log.info("Found user : {}", user);
        return user;
    }

    public User findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));

    }

    public Optional<User> findByMsisdn(String msisdn) {
        log.info("Request to find user with phone : {}", msisdn);

        return userRepository.findByMsisdn(msisdn);
    }

    public Optional<UserDTO> findByUsernameDto(Long userId) {
        log.info("User ID Serv   {} ", userId);
        return userRepository.findById(userId).map(userMapper::toDto);

    }

    // check if client exists
    public boolean doesUserExistByEmailAddress(String emailAddress) {
        Optional<User> client = userRepository.findUserByEmail(emailAddress);
        return client.isPresent();
    }

    public User addNewClient(User userDetails, Organization organization,
                             ClientRequest clientRequest) {
        log.info("add a new client");
        // Check if client already exists
        if (doesUserExistByEmailAddress(clientRequest.getEmail())) {
            throw new IllegalStateException("Client with provided email already exists");
        }

        User user = new User();
        log.info("creating new client started");
        if (userDetails != null) {
            user.setCreatedBy(userDetails.getFullName());
            user.setAddedBy(userDetails);
        }
        if (organization != null) {
            user.setCreatedBy(userDetails.getFullName());
            user.setOrganization(organization);
            //ASSIGN COACH
            Optional<User> assignedCoach = userRepository.findById(clientRequest.getCoachId());
            if (assignedCoach.isPresent()) {
                User coach1 = assignedCoach.get();
                user.setAddedBy(coach1);
            }
        }

        user.setFirstName(clientRequest.getFirstName());
        user.setLastName(clientRequest.getLastName());
        user.setFullName(clientRequest.getFirstName() + ' ' + clientRequest.getLastName());
        user.setClientType(clientRequest.getClientType());
        user.setMsisdn(clientRequest.getMsisdn());
        user.setEmail(clientRequest.getEmail());
        user.setUsername(clientRequest.getEmail());
        user.setPhysicalAddress(clientRequest.getPhysicalAddress());
        user.setClientStatus(ClientStatus.ACTIVE);
        user.setUserRole(UserRole.CLIENT);
        user.setReason(clientRequest.getReason());
        user.setPaymentMode(clientRequest.getPaymentMode());
        user.setCreatedAt(LocalDateTime.now());

        // client Number Generation
        int randNo = (int) ((Math.random() * (999 - 1)) + 1);
        String clientL = String.format("%05d", randNo);
        String clientNo = user.getFirstName().charAt(0) + user.getLastName().charAt(0) + "-" + clientL;
        user.setClientNumber(clientNo);

        user.setProfession(clientRequest.getProfession());
        User saveClient = userRepository.save(user);

        // Generate a Random 6 digit OTP - 0 - 999999
        int randomOTP = (int) ((Math.random() * (999999 - 1)) + 1);
        String token = String.format("%06d", randomOTP);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(60 * 48), // expires after 2 days of generation
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        log.info("Confirmation token generated");

        //get where the app is running
        String host = InetAddress.getLoopbackAddress().getHostName();

        //SEnding Confirmation token
        //NotificationHelper.sendConfirmationToken(token, "CONFIRM", (User) response.get(0));
        NotificationServiceHTTPClient notificationServiceHTTPClient = new NotificationServiceHTTPClient();
        String subject = "Your coachMatePro Account Has Been Created.";
        String content = "Hey, use this link to confirm your account and set your password," +
                "https://coachmate.pro/confirmclient/" + saveClient.getId() + "/" + token;
        notificationServiceHTTPClient.sendEmail(saveClient.getEmail(), subject, content, false);
        notificationServiceHTTPClient.sendSMS(saveClient.getMsisdn(), subject, content, String.valueOf(false));

        log.info(" client saved");

        log.info("client registered as user now creating wallet");
        log.info("Client registered is {}", saveClient);

        // Create client wallet
        ClientWallet clientWallet = new ClientWallet();

        if (organization != null) {
            clientWallet.setCreatedBy(organization.getOrgName());
            clientWallet.setOrganization(organization);
        }

        assert userDetails != null;
        clientWallet.setCreatedBy(userDetails.getFullName());
        clientWallet.setClient(saveClient);
        clientWallet.setWalletBalance((float) 0);
        clientWallet.setCoach(saveClient.getAddedBy());
        clientWalletRepository.save(clientWallet);
        log.info("Client Wallet created Successfully!");

        // Create client Billing Account
        ClientBillingAccount clientBillingAccount = getClientBillingAccount(userDetails, organization, saveClient);
        clientBillingAccountService.createBillingAccount(clientBillingAccount);
        log.info("Client Billing Account created Successfully!");
        return saveClient;
    }

    @NotNull
    private static ClientBillingAccount getClientBillingAccount(User userDetails, Organization organization, User saveClient) {
        ClientBillingAccount clientBillingAccount = new ClientBillingAccount();

        if (organization != null) {
            clientBillingAccount.setCreatedBy(organization.getOrgName());
            clientBillingAccount.setOrganization(organization);
        }
        clientBillingAccount.setClient(saveClient);
        clientBillingAccount.setCoach(saveClient.getAddedBy());
        clientBillingAccount.setAmountBilled((float) 0);
        clientBillingAccount.setCreatedBy(userDetails.getMsisdn());
        return clientBillingAccount;
    }

    public List<User> getClientByOrgId(Long id, UserRole userRole) {
        return userRepository.findUserByOrganizationAndUserRole(id, userRole);
    }

    public List<User> getCoachByOrganizationId(Long organizationId) {
        return userRepository.findAllByOrganizationId(organizationId);
    }

    private void savePaymentDetails(OnBoardCoachDTO onBoardCoachDTO, CoachDTO coachDTO) {
        PaymentDetailsDTO savedPaymentDetailsDTO = paymentDetailsService.findTopByCoachId(coachDTO.getId());
        if (savedPaymentDetailsDTO == null) {
            savedPaymentDetailsDTO = new PaymentDetailsDTO();
        }
        PaymentDetailsDTO paymentDetailsDTO = OnBoardCoachUtil.extractPaymentData(onBoardCoachDTO, coachDTO, savedPaymentDetailsDTO);
        paymentDetailsService.save(paymentDetailsDTO);
    }

    private void saveSettings(OnBoardCoachDTO onBoardCoachDTO, CoachDTO coachDTO) {
        CoachSettingsDTO coachSettingsDTO = coachSettingsService.findTopByCoachId(coachDTO.getId());
        if (coachSettingsDTO == null) {
            coachSettingsDTO = new CoachSettingsDTO();
        }

        coachSettingsDTO.setLogo(onBoardCoachDTO.getFilename());
        coachSettingsDTO.setCoach(coachDTO);
        coachSettingsService.save(coachSettingsDTO);
    }

    private void saveNotificationSettings(OnBoardCoachDTO onBoardCoachDTO, CoachDTO coachDTO) {
        Optional<NotificationSettingsDTO> notificationSettingsDTOOptional = notificationSettingsService.findByCoachId(coachDTO.getId());
        NotificationSettingsDTO notificationSettingsDTO = new NotificationSettingsDTO();
        if (notificationSettingsDTOOptional.isPresent()) {
            notificationSettingsDTO = notificationSettingsDTOOptional.get();
        }

        NotificationSettingsDTO notificationSettings = OnBoardCoachUtil.extractNotificationSettings(onBoardCoachDTO, coachDTO, notificationSettingsDTO);
        notificationSettingsService.save(notificationSettings);
    }

    public ContractTemplate addContractTemplates(ContractTemplatesRequest contractTemplatesRequest) {
        ContractTemplate contractTemplate = new ContractTemplate();
        contractTemplate.setCoach(contractTemplatesRequest.getCoach());
        contractTemplate.setNotesTemplate(contractTemplatesRequest.getNotesTemplate());
        contractTemplate.setServicesTemplate(contractTemplatesRequest.getServicesTemplate());
        contractTemplate.setPracticeTemplate(contractTemplatesRequest.getPracticeTemplate());
        contractTemplate.setTerms_and_conditionsTemplate(contractTemplatesRequest.getTerms_and_conditionsTemplate());
        return contractTemplatesRepository.save(contractTemplate);
    }


    public Optional<User> confirmClientTokenAndUpdatePassword(UserTokenConfirmRequest userTokenConfirmRequest) {
        log.info("Request to confirm client token and update password: {}", userTokenConfirmRequest);
        Optional<User> userOptional = userRepository.findById(userTokenConfirmRequest.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            ConfirmationToken confirmationToken = confirmationTokenService.getToken(userTokenConfirmRequest.getToken()).orElseThrow(() ->
                    new IllegalStateException("Token not Found!"));

            if (confirmationToken != null) {
                if (confirmationToken.getConfirmedAt() != null) {
                    throw new IllegalStateException("Email already confirmed");
                } else {
                    LocalDateTime expiredAt = confirmationToken.getExpiresAt();
                    if (expiredAt.isBefore(LocalDateTime.now())) {
                        throw new IllegalStateException("Token has expired");
                    } else {
                        String encodedPassword = passwordEncoder.encode(userTokenConfirmRequest.getPassword());
                        user.setPassword(encodedPassword);
                        user.setClientStatus(ClientStatus.ACTIVE);
                        userRepository.save(user);
                        confirmationTokenService.setConfirmedAt(confirmationToken.getToken());
                        enableAppUser(confirmationToken.getUser().getEmail());
                        log.info("User password updated successfully");
                        return userOptional;
                    }
                }
            } else {
                throw new IllegalStateException("Invalid Token");
            }
        } else {
            throw new IllegalStateException("Invalid Client");
        }
    }

    //Example for getting clients
    private User createExample(Long coachId, String status, String search, Long organizationId) {
        User userClientExample = new User();
        User userCoach = new User();
        Organization organization = new Organization();
        log.info("Request to get clients by coachId: {}", coachId);

        userClientExample.setUserRole(UserRole.CLIENT);
        if (organizationId != null) {
            userClientExample.setOrganization(organization);
            userClientExample.getOrganization().setId(organizationId);
        }
        if (coachId != null) {
            userClientExample.setAddedBy(userCoach);
            userClientExample.getAddedBy().setId(coachId);
        }
        if (status != null && !status.isEmpty()) {
            userClientExample.setClientStatus(ClientStatus.valueOf(status));
        }
        if (search != null && !search.isEmpty()) {
            if (search.contains("@")) {
                userClientExample.setEmail(search);
            } else if (search.startsWith("+") || search.matches("[0-9]+")) {
                userClientExample.setMsisdn(search);
            } else {
                userClientExample.setFullName(search);
            }
        }

        return userClientExample;
    }

    private User createExample_(String status, String search, Long organizationId) {
        User userCoachExample = new User();
        Organization organization = new Organization();
        log.info("Request to get coaches by orgId: {}", organizationId);

        userCoachExample.setUserRole(UserRole.COACH);
        if (organizationId != null) {
            userCoachExample.setOrganization(organization);
            userCoachExample.getOrganization().setId(organizationId);
        }

        if (status != null && !status.isEmpty()) {
            userCoachExample.setCoachStatus(CoachStatus.valueOf(status));
        }
        if (search != null && !search.isEmpty()) {
            if (search.contains("@")) {
                userCoachExample.setEmail(search);
            } else if (search.startsWith("+") || search.matches("[0-9]+")) {
                userCoachExample.setMsisdn(search);
            } else {
                // Only return coaches created by the logged in user's organization
                if (organizationId != null) {
                    userCoachExample.setFullName(search + " (Organization: " + organizationId + ")");
                } else {
                    userCoachExample.setFullName(search);
                }
            }
        }

        return userCoachExample;
    }


    public Page<ClientDTO> getClients(Long coachId, String status, String search, Long organizationId, Pageable pageable) {
        User user = createExample(coachId, status, search, organizationId);
        log.info("After example {} ", user);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("locked", "enabled", "onboarded", "addedBy.locked", "addedBy.enabled", "addedBy.onboarded")
                .withIgnoreNullValues();
        Example<User> example = Example.of(user, matcher);

        //return example
        return userRepository.findAll(example, pageable).map(clientMapper::toDto);
    }

    public Page<CoachDTO> getCoaches(
            String status,
            String search,
            Long organizationId,
            Pageable pageable) {
        User user = createExample_(status, search, organizationId);
        log.info("After example {} ", user);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("locked", "enabled", "onboarded", "addedBy.locked", "addedBy.enabled", "addedBy.onboarded")
                .withIgnoreNullValues();

        Example<User> example = Example.of(user, matcher);
        log.info("\nGot coaches {} \n", userRepository.findAll(example, pageable));
        return userRepository.findAll(example, pageable).map(coachMapper::toDto);
    }

    public User getClientById(Long clientId, User userDetails) {
        log.info("Request to get client by id: {}", clientId);
        Optional<User> userOptional = userRepository.findById(clientId);
        return userOptional.orElseThrow(() -> new IllegalStateException("Client not found"));
    }


    public User getCoachById(Long coachId, User userDetails) {
        log.info("Request to get coach by id: {}", coachId);
        Optional<User> userOptional = userRepository.findById(coachId);
        return userOptional.orElseThrow(() -> new IllegalStateException("Coach not found"));
    }

    public Optional<User> confirmCoachTokenAndUpdatePassword(UserTokenConfirmRequest userTokenConfirmRequest) {
        log.info("Request to confirm coach token and update password: {}", userTokenConfirmRequest);
        Optional<User> userOptional = userRepository.findById(userTokenConfirmRequest.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            ConfirmationToken confirmationToken = confirmationTokenService.getToken(userTokenConfirmRequest.getToken()).orElseThrow(() ->
                    new IllegalStateException("Token not Found!"));

            if (confirmationToken != null) {
                if (confirmationToken.getConfirmedAt() != null) {
                    throw new IllegalStateException("Email already confirmed");
                } else {
                    LocalDateTime expiredAt = confirmationToken.getExpiresAt();
                    if (expiredAt.isBefore(LocalDateTime.now())) {
                        throw new IllegalStateException("Token has expired");
                    } else {
                        String encodedPassword = passwordEncoder.encode(userTokenConfirmRequest.getPassword());
                        user.setPassword(encodedPassword);
                        user.setCoachStatus(CoachStatus.ACTIVE);
                        userRepository.save(user);
                        confirmationTokenService.setConfirmedAt(confirmationToken.getToken());
                        enableAppUser(confirmationToken.getUser().getEmail());
                        log.info("User password updated successfully");
                        return userOptional;
                    }
                }
            } else {
                throw new IllegalStateException("Invalid Token");
            }
        } else {
            throw new IllegalStateException("Invalid Coach");
        }
    }

    @Transactional
    public ClientDTO updateClientStatus(Long id, ClientStatus clientStatus) {
        log.info("Changing status of client {} to status {}", id, clientStatus);
        Optional<User> clientOptional = userRepository.findById(id);

        if (clientOptional.isEmpty()) {
            throw new IllegalStateException("Client doesn't exist");
        }

        User client = clientOptional.get();

        if (client.getClientStatus() == ClientStatus.SUSPENDED) {
            log.info("Client {} is in suspended state", id);
            throw new IllegalStateException("Client is in Suspended State");
        } else if (Objects.equals(clientStatus, ClientStatus.CLOSED)) {
            client.setClientStatus(ClientStatus.CLOSED);
        } else if (Objects.equals(clientStatus, ClientStatus.SUSPENDED)) {
            client.setClientStatus(ClientStatus.SUSPENDED);
        } else if (Objects.equals(clientStatus, ClientStatus.ACTIVE)) {
            client.setClientStatus(ClientStatus.ACTIVE);
        } else {
            throw new IllegalStateException("Invalid Status");
        }


        client = userRepository.save(client);
        log.info("Client with id {} changed status to {}", id, clientStatus);
        return clientMapper.toDto(client);

    }

    public void saveGoogleAuthData(String googleAuthData, User userDetails) {
        log.info("Request to save google auth data: {}", googleAuthData);
        Optional<User> userOptional = userRepository.findById(userDetails.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setGoogle(googleAuthData);
            userRepository.save(user);
        } else {
            throw new IllegalStateException("User not found");
        }
    }

    public void removeGoogleAuthData(User userDetails) {
        log.info("Request to remove google auth data");
        Optional<User> userOptional = userRepository.findById(userDetails.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setGoogle(null);
            userRepository.save(user);
        } else {
            throw new IllegalStateException("User not found");
        }
    }

    public void addCalendlyUsername(String calendlyUsername, User userDetails) {
        log.info("Request to add calendly username: {}", calendlyUsername);
        Optional<User> userOptional = userRepository.findById(userDetails.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setCalendlyUsername(calendlyUsername);
            userRepository.save(user);
        } else {
            throw new IllegalStateException("User not found");
        }
    }

    public User getAddedBy(Long id) {
        log.info("Request to get added by: {}", id);
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getAddedBy();
        } else {
            throw new IllegalStateException("User not found");
        }
    }

    public CoachDTO saveCoach(CoachDTO coachDTO) {
        User existingUser = userRepository.findById(coachDTO.getId()).orElse(null);

        if (existingUser != null) {
            if (coachDTO.getId() == null) {
                coachDTO.setCreatedBy(SecurityUtils.getCurrentUsername());
                coachDTO.setCreatedAt(LocalDateTime.now());
            } else {
                coachDTO.setLastUpdatedBy(SecurityUtils.getCurrentUsername());
                coachDTO.setLastUpdatedAt(LocalDateTime.now());
            }

            existingUser.setBusinessName(coachDTO.getBusinessName());
            existingUser.setFirstName(coachDTO.getFirstName());
            existingUser.setLastName(coachDTO.getLastName());
            existingUser.setEmail(coachDTO.getEmail());
            existingUser.setMsisdn(coachDTO.getMsisdn());
            existingUser.setFullName(coachDTO.getFirstName() + ' ' + coachDTO.getLastName());

            userRepository.save(existingUser);
        } else {
            throw new UserNotFoundException("User with ID " + coachDTO.getId() + " not found");
        }

        return coachDTO;
    }

    public ClientDTO saveClient(ClientDTO clientDTO) {
        User existingUser = userRepository.findById(clientDTO.getId()).orElse(null);

        if (existingUser != null) {
            if (clientDTO.getId() == null) {
                clientDTO.setCreatedBy(SecurityUtils.getCurrentUsername());
                clientDTO.setCreatedAt(LocalDateTime.now());
            } else {
                clientDTO.setLastUpdatedBy(SecurityUtils.getCurrentUsername());
                clientDTO.setLastUpdatedAt(LocalDateTime.now());
            }

            existingUser.setMsisdn(clientDTO.getMsisdn());
            existingUser.setFirstName(clientDTO.getFirstName());
            existingUser.setLastName(clientDTO.getLastName());
            existingUser.setEmail(clientDTO.getEmail());
            existingUser.setFullName(clientDTO.getFirstName() + ' ' + clientDTO.getLastName());


            // Update other client-related properties...

            userRepository.save(existingUser);
        } else {
            throw new UserNotFoundException("User with ID " + clientDTO.getId() + " not found");
        }

        return clientDTO;
    }

    public OrganizationDTO saveOrganization(OrganizationDTO organizationDTO) {
        User existingUser = userRepository.findById(organizationDTO.getId()).orElse(null);
        if (existingUser != null) {
            if (organizationDTO.getId() == null) {
                organizationDTO.setCreatedBy(SecurityUtils.getCurrentUsername());
                organizationDTO.setCreatedAt(LocalDateTime.now());
            } else {
                organizationDTO.setLastUpdatedBy(SecurityUtils.getCurrentUsername());
                organizationDTO.setLastUpdatedAt(LocalDateTime.now());
            }

            existingUser.setMsisdn(organizationDTO.getMsisdn());
            existingUser.setFirstName(organizationDTO.getFirstName());
            existingUser.setLastName(organizationDTO.getLastName());
            existingUser.setEmail(organizationDTO.getEmail());
            existingUser.setFullName(organizationDTO.getFirstName() + ' ' + organizationDTO.getLastName());
            existingUser.setBusinessName(organizationDTO.getOrgName());


            userRepository.save(existingUser);
        } else {
            throw new UserNotFoundException("User with ID " + organizationDTO.getId() + " not found");
        }

return organizationDTO;
    }
}