package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.enums.UserRole;
import com.natujenge.thecouch.web.rest.dto.UserDTO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    List<User> findUserByOrganizationAndUserRole(Long id, UserRole userRole);

    List<User> findAllByOrganizationId(Long organizationId);

    Optional<User> findByIdAndOrganizationId(Long clientId, Long id);

    Optional<UserDTO> getByMsisdn(String msisdn);

    Optional<User> findUserById(Long userId);

}

