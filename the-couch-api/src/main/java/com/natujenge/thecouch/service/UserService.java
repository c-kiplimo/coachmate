package com.natujenge.thecouch.service;

import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.domain.enums.ClientStatus;
import com.natujenge.thecouch.domain.enums.ContentStatus;
import com.natujenge.thecouch.domain.enums.UserRole;
import com.natujenge.thecouch.repository.ClientWalletRepository;
import com.natujenge.thecouch.repository.ContractTemplatesRepository;
import com.natujenge.thecouch.repository.UserRepository;
import com.natujenge.thecouch.service.dto.*;
import com.natujenge.thecouch.service.mapper.CoachMapper;
import com.natujenge.thecouch.service.mapper.UserMapper;
import com.natujenge.thecouch.util.OnBoardCoachUtil;
import com.natujenge.thecouch.web.rest.request.ClientRequest;
import com.natujenge.thecouch.web.rest.request.ContractTemplatesRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "User with Email %s not found!";
    private final static String USER_EXISTS = "Email %s Taken!";
    private final CoachMapper coachMapper;
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

    public UserService(CoachMapper coachMapper, UserRepository userRepository, @Lazy RegistrationService registrationService, ClientWalletRepository clientWalletRepository, ClientBillingAccountService clientBillingAccountService, PaymentDetailsService paymentDetailsService, CoachSettingsService coachSettingsService, NotificationSettingsService notificationSettingsService, ContractTemplatesRepository contractTemplatesRepository, PasswordEncoder passwordEncoder, ConfirmationTokenService confirmationTokenService, UserMapper userMapper) {
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
    }

    public User enableAppUser(String email) {

        // Request UserDto rather than all details
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));

        user.setEnabled(true);

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
                LocalDateTime.now().plusMinutes(60*48), // expires after 2 days of generation
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
    public List<Object> signupCoachAsUser(User user ) {
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
                LocalDateTime.now().plusMinutes(60*48),
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

        if(optionalUser.isEmpty()){
            throw new IllegalStateException("User with username " + registeredUser.getUsername() + " not found!");
        }

        User user = optionalUser.get();
        user.setNotificationSettings(registeredUser.getNotificationSettings());
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
        return userRepository.findById(userId).map(userMapper::toDto);

    }

    // check if client exists
    public boolean doesUserExistByEmailAddress(String emailAddress){
        Optional<User> client = userRepository.findUserByEmail(emailAddress);
        return client.isPresent();
    }

    public User addNewClient(User userDetails,Organization organization, ClientRequest clientRequest, String msisdn) {
        log.info("add a new client");
        // Check if client already exists
        if (doesUserExistByEmailAddress(clientRequest.getEmail())) {
            throw new IllegalStateException("Client with provided email already exists");
        }

        User saveClient = null;
        User user = new User(clientRequest.getFirstName(), clientRequest.getLastName(), clientRequest.getEmail(), clientRequest.getMsisdn(), UserRole.CLIENT, organization);
        log.info("creating new client started");

        if (userDetails != null) {
            user.setCreatedBy(userDetails.getFullName());
            user.setAddedBy(userDetails);
        }
        if (organization != null) {
            user.setCreatedBy(organization.getOrgName());
            user.setOrganization(organization);
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
        user.setPhysicalAddress(clientRequest.getPhysicalAddress());
        user.setClientStatus(ClientStatus.ACTIVE);
        user.setReason(clientRequest.getReason());
        user.setPaymentMode(clientRequest.getPaymentMode());
        user.setCreatedAt(LocalDateTime.now());

        // client Number Generation
        int randNo = (int) ((Math.random() * (999 - 1)) + 1);
        String clientL = String.format("%05d", randNo);
        String clientNo = user.getFirstName().charAt(0) + user.getLastName().charAt(0) + "-" + clientL;
        user.setClientNumber(clientNo);

        user.setProfession(clientRequest.getProfession());

        saveClient = userRepository.save(user);
        log.info(" client saved");
        log.info("registering client as user begins");


       // registrationService.registerClientAsUser(clientRequest, organization, saveClient);
        log.info("client registered as user now creating wallet");
        log.info("Client registered is {}", saveClient);

        // Create client wallet
        ClientWallet clientWallet = new ClientWallet();

        if (organization != null) {
            clientWallet.setCreatedBy(organization.getOrgName());
            clientWallet.setOrganization(organization);
        }

        clientWallet.setClient(saveClient);
        clientWallet.setWalletBalance(Float.valueOf(0));
        clientWalletRepository.save(clientWallet);
        log.info("Client Wallet created Successfully!");

        // Create client Billing Account
        ClientBillingAccount clientBillingAccount = new ClientBillingAccount();

        if (organization != null) {
            clientBillingAccount.setCreatedBy(organization.getOrgName());
            clientBillingAccount.setOrganization(organization);
        }
        clientBillingAccount.setClient(saveClient);
        clientBillingAccount.setAmountBilled((float) 0);
        clientBillingAccount.setCreatedBy(msisdn);
        clientBillingAccountService.createBillingAccount(clientBillingAccount);
        log.info("Client Billing Account created Successfully!");
        return saveClient;
    }

    public List<User> getClientByOrgId(Long id, UserRole userRole) {
        return userRepository.findUserByOrganizationAndUserRole(id, userRole);
    }

    public List<User> getCoachByOrganizationId(Long organizationId) {
        return userRepository.findAllByOrganizationId(organizationId);
    }

    private void savePaymentDetails(OnBoardCoachDTO onBoardCoachDTO, CoachDTO coachDTO) {
        PaymentDetailsDTO savedPaymentDetailsDTO = paymentDetailsService.findTopByCoachId(coachDTO.getId());
        if (savedPaymentDetailsDTO == null){
            savedPaymentDetailsDTO = new PaymentDetailsDTO();
        }
        PaymentDetailsDTO paymentDetailsDTO = OnBoardCoachUtil.extractPaymentData(onBoardCoachDTO, coachDTO, savedPaymentDetailsDTO);
        paymentDetailsService.save(paymentDetailsDTO);
    }

    private void saveSettings(OnBoardCoachDTO onBoardCoachDTO, CoachDTO coachDTO){
        CoachSettingsDTO coachSettingsDTO = coachSettingsService.findTopByCoachId(coachDTO.getId());
        if (coachSettingsDTO == null){
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

        NotificationSettingsDTO notificationSettings = OnBoardCoachUtil.extractNotificationSettings(onBoardCoachDTO, coachDTO,notificationSettingsDTO);
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

    public Optional<UserDTO> findOne(Long id) {
        log.info("Request to get Coach by id: {}", id);
        //Optional<User> userOptional = userRepository.findById(id);
       // return userOptional.map(coachMapper::toDto);
        return userRepository.findById(id).map(coachMapper::toDto);
    }

}
