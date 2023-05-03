package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    User findUserByUsername(String username);

    Optional<User> findByMsisdn(String msisdn);


    Optional<User> findUserByEmail(String emailAddress);


    Optional<User> findByIdAndAddedById(Long clientId, Long coachId);

    List<User> getUserByOrganisationAndUserRole(Long id, UserRole userRole);

    List<User> findAllByOrganizationId(Long organizationId);
}

