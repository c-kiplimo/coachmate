package com.natujenge.thecouch.service;
import com.natujenge.thecouch.domain.Coach;
import com.natujenge.thecouch.domain.Organization;
import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.repository.CoachRepository;
import com.natujenge.thecouch.repository.OrganizationRepository;
import com.natujenge.thecouch.repository.UserRepository;
import com.natujenge.thecouch.web.rest.request.CoachRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CoachService {

    private final CoachRepository coachRepository;
    private final OrganizationRepository organizationRepository;
    private final RegistrationService registrationService;
    private final UserRepository userRepository;
    private final UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    //     constructor
    public CoachService(CoachRepository coachRepository, OrganizationRepository organizationRepository, RegistrationService registrationService, UserRepository userRepository, RegistrationService registrationService1, UserRepository userRepository1, UserService userService) {
        this.coachRepository = coachRepository;
        this.organizationRepository = organizationRepository;
        this.registrationService = registrationService1;
        this.userRepository = userRepository1;
        this.userService = userService;
    }

    // create coach by organization
    public void   addNewCoachByOrganization(Organization organization, String msisdn, CoachRequest coachRequest) {
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
        String coachNo = coach.getLastName().substring(0, 2) +
                coach.getFirstName().charAt(0) + coach.getLastName().charAt(0) + "-" + coachL;
        coach.setCoachNumber(coachNo);
        // save coach
        Coach savedCoach = coachRepository.save(coach);
        registrationService.registerCoachAsUser(coachRequest,organization,savedCoach);



        log.info("coach registered successfully");

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
}
