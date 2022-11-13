package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.UserSession;
import com.natujenge.thecouch.dto.RestResponse;
import com.natujenge.thecouch.repository.UserSessionRepository;
import com.natujenge.thecouch.security.JwtTokenUtil;
import com.natujenge.thecouch.service.UserService;
import com.natujenge.thecouch.web.rest.request.Login;
import com.natujenge.thecouch.web.rest.request.LoginToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/token")
@Slf4j
public class AuthenticationResource {

    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserSessionRepository userSessionRepo;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping
    public ResponseEntity<?> generateToken(@RequestBody Login login) {
        try {

            log.info("{}", login);

            User user = userService.findByUsername(login.getUsername());

            if (user == null) {
                String message = String.format("User %s not found", login.getUsername());
                log.info(message);
                return new ResponseEntity<>(new RestResponse(true, "User not Found."), HttpStatus.UNAUTHORIZED);
            }

            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserSession userSession = userSessionRepo.findByUser(user);

            // save session if none exists, LoggedIn == 1, logged in user
            if(userSession ==  null){
                userSession = new UserSession();
                userSession.setUser(user);
                userSession.setLoggedIn(1);
                userSessionRepo.save(userSession);
            }

            // Since session already exists, increment loginTrials
            userSession.setLoginTrials(userSession.getLoginTrials()+1);
            userSessionRepo.save(userSession);

            String token = jwtTokenUtil.generateToken(user);

            return new ResponseEntity<>(new LoginToken(user, token), HttpStatus.OK);

        } catch (AuthenticationException authe){
            String message = String.format("Authentication error for  %s", login.getUsername());
            log.error("Authentication error for  {} Ex: {}", login.getUsername(), authe.getMessage());
            return new ResponseEntity<>(new RestResponse(true, "Wrong username/Password."), HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            String message = String.format("Internal server error on login  %s", login.getUsername());
            log.error("Error occurred while calling generateToken ", e);
            return new ResponseEntity<>(new RestResponse(true, "Error occurred, try again later"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/logout/{token}")
    public ResponseEntity<?> logout(@PathVariable("token") String token) {
        try{

            if(token == null || token.equals("")){
                return new ResponseEntity<>(new RestResponse(true, "Failed to logout, token is empty"), HttpStatus.OK);
            }

            String username = jwtTokenUtil.getUsernameUnlimitedSkew(token);
            User user = userService.findByUsername(username);
            if(log.isDebugEnabled()){
                log.debug("Received a request to log out {}", username);
            }
            UserSession userSession = userSessionRepo.findByUser(user);

            if(userSession == null){
                return new ResponseEntity<>(new RestResponse(false, "User session not found"), HttpStatus.OK);
            }

            if(userSession.getLoggedIn() == 0){
                return new ResponseEntity<>(new RestResponse(false, "User already logged out"), HttpStatus.OK);
            }
            userSession.setLoggedIn(0);
            userSessionRepo.save(userSession);
            return new ResponseEntity<>(new RestResponse(false, "User logged out"), HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occurred while calling {} for {} Ex: {}", "logout", token, e);
            return new ResponseEntity<>(new RestResponse(true, "Failed to Logout, Try Later"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
