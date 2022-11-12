package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession,Long> {

    UserSession findByUser(User user);

}
