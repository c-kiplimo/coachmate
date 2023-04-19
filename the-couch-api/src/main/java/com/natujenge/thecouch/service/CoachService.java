package com.natujenge.thecouch.service;



import com.natujenge.thecouch.domain.*;
import com.natujenge.thecouch.repository.*;
import com.natujenge.thecouch.security.SecurityUtils;
import com.natujenge.thecouch.service.dto.*;
import com.natujenge.thecouch.service.mapper.CoachMapper;
import com.natujenge.thecouch.service.mapper.NotificationSettingsMapper;
import com.natujenge.thecouch.repository.CoachRepository;
import com.natujenge.thecouch.repository.CoachWalletRepository;
import com.natujenge.thecouch.repository.OrganizationRepository;
import com.natujenge.thecouch.repository.UserRepository;

import com.natujenge.thecouch.web.rest.request.CoachRequest;
import com.natujenge.thecouch.util.OnBoardCoachUtil;
import com.natujenge.thecouch.web.rest.request.ContractTemplatesRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CoachService {

    private final CoachRepository coachRepository;
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
    @Autowired
    CoachWalletRepository coachWalletRepository;
    @Autowired
    CoachBillingAccountService coachBillingAccountService;
    @Autowired
    PasswordEncoder passwordEncoder;

    //     constructor
    public CoachService(CoachRepository coachRepository, OrganizationRepository organizationRepository, ContractTemplatesRepository contractTemplatesRepository, RegistrationService registrationService, UserRepository userRepository, CoachSettingsService coachSettingsService, UserService userService, NotificationSettingsService notificationSettingsService, PaymentDetailsService paymentDetailsService, CoachMapper coachMapper, NotificationSettingsMapper notificationSettingsMapper, NotificationSettingsRepository notificationSettingsRepository) {
        this.coachRepository = coachRepository;
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
    }

    // create coach by organization
    public Coach   addNewCoachByOrganization(Organization organization, String msisdn, CoachRequest coachRequest) {
        log.info("add a new coach to database by organization{}",organization.getId());

        Optional<User>  optionalUser = userService.findByMsisdn(coachRequest.getMsisdn());
        if (optionalUser.isPresent()){
          throw new IllegalArgumentException("User already exists!");
        }

        Coach coach = new Coach();
        coach.setFirstName(coachRequest.getFirstName());
        coach.setLastName(coachRequest.getLastName());
        coach.setFullName(coachRequest.getFirstName() + " " + coachRequest.getLastName());
        coach.setMsisdn(coachRequest.getMsisdn());
        coach.setEmailAddress(coachRequest.getEmail());
        coach.setCreatedBy(msisdn);
        coach.setOrganization(organization);

        // coach Number Generation
        int randNo = (int) ((Math.random() * (999 - 1)) + 1);
        String coachL = String.format("%05d", randNo);
        String coachNo =  coach.getFirstName().charAt(0) + coach.getLastName().charAt(0) + "-" + coachL;
        coach.setCoachNumber(coachNo);



        Coach savedCoach = coachRepository.save(coach);

        registrationService.registerCoachAsUser(coachRequest, organization, savedCoach);

        // Create client wallet
        CoachWallet coachWallet = new CoachWallet();


        if(organization != null){
            coachWallet.setOrganization(organization);
        }

        coachWallet.setCoach(savedCoach);
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
    public Optional<Coach> findCoachById(long id) {
        return coachRepository.findCoachById(id);
    }


    public List<Coach> getCoachByOrganizationId(Long organizationId) {
        return coachRepository.findAllByOrganizationId(organizationId);
    }

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
    public Optional<CoachDTO> findOne(Long id) {
        log.info("Request to get Coach by id: {}", id);
        return coachRepository.findById(id).map(coachMapper::toDto);
    }

    public CoachDTO onBoard(OnBoardCoachDTO onBoardCoachDTO){
        Optional<CoachDTO> coachDTOOptional = findOne(onBoardCoachDTO.getCoachId());
        if (coachDTOOptional.isEmpty()){
            throw new IllegalStateException("Coach with Id " + onBoardCoachDTO.getCoachId() + " does not exist");
        }

        CoachDTO coachDTO = coachDTOOptional.get();

        //Save settings
        saveSettings(onBoardCoachDTO, coachDTO);

        //Save Notifications
        saveNotificationSettings(onBoardCoachDTO, coachDTO);

        //Save payment Details
        savePaymentDetails(onBoardCoachDTO,coachDTO);



        coachDTO.setOnboarded(true);
        save(coachDTO);

        return coachDTO;
    }
    @Transactional
    public CoachDTO save(CoachDTO coachDTO){
        if (coachDTO.getId()== null){
            coachDTO.setCreatedBy(SecurityUtils.getCurrentUsername());
            coachDTO.setCreatedAt(LocalDateTime.now());
        } else {
            coachDTO.setLastUpdatedBy(SecurityUtils.getCurrentUsername());
            coachDTO.setLastUpdatedAt(LocalDateTime.now());
        }

        Coach coach = coachMapper.toEntity(coachDTO);
        coach = coachRepository.save(coach);

        return coachMapper.toDto(coach);
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

    private void savePaymentDetails(OnBoardCoachDTO onBoardCoachDTO, CoachDTO coachDTO) {
        PaymentDetailsDTO savedPaymentDetailsDTO = paymentDetailsService.findTopByCoachId(coachDTO.getId());
        if (savedPaymentDetailsDTO == null){
            savedPaymentDetailsDTO = new PaymentDetailsDTO();
        }
        PaymentDetailsDTO paymentDetailsDTO = OnBoardCoachUtil.extractPaymentData(onBoardCoachDTO, coachDTO, savedPaymentDetailsDTO);
        paymentDetailsService.save(paymentDetailsDTO);
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
}
