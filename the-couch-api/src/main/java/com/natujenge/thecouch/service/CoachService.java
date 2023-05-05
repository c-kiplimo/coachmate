package com.natujenge.thecouch.service;


import com.natujenge.thecouch.domain.CoachBillingAccount;
import com.natujenge.thecouch.domain.CoachWallet;
import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.*;
import com.natujenge.thecouch.service.mapper.CoachMapper;
import com.natujenge.thecouch.service.mapper.NotificationSettingsMapper;
import com.natujenge.thecouch.web.rest.request.CoachRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CoachService {


    private final OrganizationRepository organizationRepository;
    private final ContractTemplatesRepository contractTemplatesRepository;
    private final RegistrationService registrationService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final  NotificationSettingsService notificationSettingsService;
    private final CoachSettingsService coachSettingsService;
    private final PaymentDetailsService paymentDetailsService;
    private final CoachMapper coachMapper;
    private final NotificationSettingsMapper notificationSettingsMapper;
    private final NotificationSettingsRepository notificationSettingsRepository;

    private final CoachWalletRepository coachWalletRepository;

    private final CoachBillingAccountService coachBillingAccountService;

    private final PasswordEncoder passwordEncoder;

    //     constructor
    public CoachService(OrganizationRepository organizationRepository, ContractTemplatesRepository contractTemplatesRepository, RegistrationService registrationService, UserRepository userRepository, CoachSettingsService coachSettingsService, UserService userService, NotificationSettingsService notificationSettingsService, PaymentDetailsService paymentDetailsService, CoachMapper coachMapper, NotificationSettingsMapper notificationSettingsMapper, NotificationSettingsRepository notificationSettingsRepository, CoachWalletRepository coachWalletRepository, CoachBillingAccountService coachBillingAccountService, PasswordEncoder passwordEncoder) {

        this.organizationRepository = organizationRepository;
        this.contractTemplatesRepository = contractTemplatesRepository;
        this.registrationService = registrationService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.notificationSettingsService = notificationSettingsService;
        this.coachSettingsService = coachSettingsService;
        this.paymentDetailsService = paymentDetailsService;
        this.coachMapper = coachMapper;
        this.notificationSettingsMapper = notificationSettingsMapper;
        this.notificationSettingsRepository = notificationSettingsRepository;
        this.coachWalletRepository = coachWalletRepository;
        this.coachBillingAccountService = coachBillingAccountService;
        this.passwordEncoder = passwordEncoder;
    }

    // create coach by organization

//    public Coach   addNewCoachByOrganization(Organization organization, String msisdn, CoachRequest coachRequest) {
//        log.info("add a new coach to database by organization{}",organization.getId());
//
//        Optional<User>  optionalUser = userService.findByMsisdn(coachRequest.getMsisdn());
//        if (optionalUser.isPresent()){
//          throw new IllegalArgumentException("User already exists!");
//        }
//
//        Coach coach = new Coach();
//        coach.setFirstName(coachRequest.getFirstName());
//        coach.setLastName(coachRequest.getLastName());
//        coach.setFullName(coachRequest.getFirstName() + " " + coachRequest.getLastName());
//        coach.setMsisdn(coachRequest.getMsisdn());
//        coach.setEmailAddress(coachRequest.getEmail());
//        coach.setCreatedBy(msisdn);
//        coach.setOrganization(organization);
//
//        // coach Number Generation
//        int randNo = (int) ((Math.random() * (999 - 1)) + 1);
//        String coachL = String.format("%05d", randNo);
//        String coachNo =  coach.getFirstName().charAt(0) + coach.getLastName().charAt(0) + "-" + coachL;
//        coach.setCoachNumber(coachNo);
//
//
//
//        User savedCoach = coachRepository.save(coach);
//
//        registrationService.registerCoachAsUser(coachRequest, organization, savedCoach);
//
//        // Create client wallet
//        CoachWallet coachWallet = new CoachWallet();
//
//
//        if(organization != null){
//            coachWallet.setOrganization(organization);
//        }
//
//        coachWallet.setCoach(savedCoach);
//        coachWallet.setWalletBalance(Float.valueOf(0));
//        coachWallet.setCreatedBy(msisdn);
//        coachWalletRepository.save(coachWallet);
//        log.info("Client Wallet created Successfully!");
//
//        // Create client Billing Account
//        CoachBillingAccount coachBillingAccount = new CoachBillingAccount();
//
//        if(organization != null){
//            coachBillingAccount.setOrganization(organization);
//        }
//        coachBillingAccount.setCoach(savedCoach);
//        coachBillingAccount.setAmountBilled((float) 0);
//        coachBillingAccount.setCreatedBy(msisdn);
//        coachBillingAccountService.createBillingAccount(coachBillingAccount);
//        log.info("Client Billing Account created Successfully!");
//        return savedCoach;
//
//    }
    public User   addNewCoachByOrganization(Organization organization, String msisdn, CoachRequest coachRequest) {
        log.info("add a new coach to database by organization{}",organization.getId());

        Optional<User>  optionalUser = userService.findByMsisdn(coachRequest.getMsisdn());
        if (optionalUser.isPresent()){
          throw new IllegalArgumentException("User already exists!");
        }

        User coach = new User();
        coach.setFirstName(coachRequest.getFirstName());
        coach.setLastName(coachRequest.getLastName());
        coach.setFullName(coachRequest.getFirstName() + " " + coachRequest.getLastName());
        coach.setMsisdn(coachRequest.getMsisdn());
        coach.setEmail(coachRequest.getEmail());
        coach.setCreatedBy(msisdn);
        coach.setOrganization(organization);

        // coach Number Generation
        int randNo = (int) ((Math.random() * (999 - 1)) + 1);
        String coachL = String.format("%05d", randNo);
        String coachNo =  coach.getFirstName().charAt(0) + coach.getLastName().charAt(0) + "-" + coachL;
        coach.setCoachNumber(coachNo);



        User savedCoach = userRepository.save(coach);

        registrationService.registerCoachAsUser(coachRequest, organization, savedCoach);

        // Create client wallet
        CoachWallet coachWallet = new CoachWallet();


        if(organization != null){
            coachWallet.setOrganization(organization);
        }

        coachWallet.setUser(savedCoach);
        coachWallet.setWalletBalance(Float.valueOf(0));
        coachWallet.setCreatedBy(msisdn);
        coachWalletRepository.save(coachWallet);
        log.info("Client Wallet created Successfully!");

        // Create client Billing Account
        CoachBillingAccount coachBillingAccount = new CoachBillingAccount();

        if(organization != null){
            coachBillingAccount.setOrganization(organization);
        }
        coachBillingAccount.setCoach(savedCoach);
        coachBillingAccount.setAmountBilled((float) 0);
        coachBillingAccount.setCreatedBy(msisdn);
        coachBillingAccountService.createBillingAccount(coachBillingAccount);
        log.info("Client Billing Account created Successfully!");
        return savedCoach;

    }

    //SHOW - one coach
//    public Optional<Coach> findCoachById(long id) {
//        return coachRepository.findCoachById(id);
//    }




    public Optional<User> confirmCoachTokenAndUpdatePassword(CoachRequest coachRequest) {

            Optional<User> userOptional = userRepository.findById(coachRequest.getId());
            if (userOptional.isEmpty()) {
                throw new IllegalStateException("Coach User Not Found!!");
            }

            String TokenConfirm = registrationService.confirmToken(coachRequest.getToken());
            if (!TokenConfirm.isEmpty()) {
                User user = userOptional.get();

                //Encode Password
                String encodedPassword = passwordEncoder.encode(coachRequest.getPassword());
                user.setPassword(encodedPassword);

                user = userRepository.save(user);

                log.info("Password Updated Successfully");
                return Optional.of(user);
            }
            return userOptional;

    }
//    public Optional<CoachDTO> findOne(Long id) {
//        log.info("Request to get Coach by id: {}", id);
//        return coachRepository.findById(id).map(coachMapper::toDto);
//    }

//    public CoachDTO onBoard(OnBoardCoachDTO onBoardCoachDTO){
//        Optional<CoachDTO> coachDTOOptional = findOne(onBoardCoachDTO.getCoachId());
//        if (coachDTOOptional.isEmpty()){
//            throw new IllegalStateException("Coach with Id " + onBoardCoachDTO.getCoachId() + " does not exist");
//        }
//
//        CoachDTO coachDTO = coachDTOOptional.get();
//
//        //Save settings
//        saveSettings(onBoardCoachDTO, coachDTO);
//
//        //Save Notifications
//        saveNotificationSettings(onBoardCoachDTO, coachDTO);
//
//        //Save payment Details
//        savePaymentDetails(onBoardCoachDTO,coachDTO);
//
//
//
//        coachDTO.setOnboarded(true);
//        save(coachDTO);
//
//        return coachDTO;
//    }
//    @Transactional
//    public CoachDTO save(CoachDTO coachDTO){
//        if (coachDTO.getId()== null){
//            coachDTO.setCreatedBy(SecurityUtils.getCurrentUsername());
//            coachDTO.setCreatedAt(LocalDateTime.now());
//        } else {
//            coachDTO.setLastUpdatedBy(SecurityUtils.getCurrentUsername());
//            coachDTO.setLastUpdatedAt(LocalDateTime.now());
//        }
//
//        Coach coach = coachMapper.toEntity(coachDTO);
//        coach = coachRepository.save(coach);
//
//        return coachMapper.toDto(coach);
//    }







}
