package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    User findUserByUsername(String username);

    Optional<User> findByMsisdn(String msisdn);

    Optional<User> findUserByEmail(String emailAddress);

}
