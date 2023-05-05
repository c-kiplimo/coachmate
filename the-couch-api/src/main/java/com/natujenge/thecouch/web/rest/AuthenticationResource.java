package com.natujenge.thecouch.web.rest;

import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.UserSession;
import com.natujenge.thecouch.service.dto.UserDTO;
import com.natujenge.thecouch.service.mapper.UserMapper;
import com.natujenge.thecouch.web.rest.dto.RestResponse;
import com.natujenge.thecouch.repository.UserSessionRepository;
import com.natujenge.thecouch.security.JwtTokenUtil;
import com.natujenge.thecouch.service.UserService;
import com.natujenge.thecouch.web.rest.request.Login;
import com.natujenge.thecouch.web.rest.request.LoginToken;
import lombok.extern.slf4j.Slf4j;
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

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final UserSessionRepository userSessionRepo;

    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;

    public AuthenticationResource(UserService userService, AuthenticationManager authenticationManager, UserSessionRepository userSessionRepo, JwtTokenUtil jwtTokenUtil, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userSessionRepo = userSessionRepo;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userMapper = userMapper;
    }

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
                    new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("User authenticated ");

            UserSession userSession = userSessionRepo.findByUserId(user.getId());
            log.info("User session found");

            // save session if none exists, LoggedIn == 1, logged in user
            if(userSession == null){
                userSession = new UserSession();
                userSession.setUser(user);
                userSession.setLoggedIn(1);
                userSessionRepo.save(userSession);
                log.info("User seesion is null. saved new ");

            }

            log.info("Logging in {}", login);

            // Since session already exists, increment loginTrials
            userSession.setLoginTrials(userSession.getLoginTrials()+1);
            userSessionRepo.save(userSession);
            log.info("User session saved ");

            String token = jwtTokenUtil.generateToken(user);
            log.info("User token generate  {} ", token);
            UserDTO userDto = userMapper.toDto(user);
            log.info("User found of msisdn   {} ", user.getId());
            log.info("User DTO   {} ", userDto);

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
