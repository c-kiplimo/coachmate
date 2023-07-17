package com.natujenge.thecouch.security;

import com.natujenge.thecouch.domain.User;
import com.natujenge.thecouch.domain.UserSession;
import com.natujenge.thecouch.repository.UserSessionRepository;
import com.natujenge.thecouch.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.natujenge.thecouch.config.Constants.HEADER_STRING;
import static com.natujenge.thecouch.config.Constants.TOKEN_PREFIX;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserService userService;

    @Autowired
    UserSessionRepository userSessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest req, @NotNull HttpServletResponse res, @NotNull FilterChain chain)
            throws IOException, ServletException {
        final String header = req.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            authToken = header.replace(TOKEN_PREFIX, "");
            if(authToken != null && !authToken.equals("")) {
                try {
                    username = jwtTokenUtil.getUsernameFromToken(authToken);
                } catch (final IllegalArgumentException e) {
                    logger.error("an error occurred during getting username from token {}", e.getCause());

                } catch (final ExpiredJwtException e) {
                    logger.info("User token has expired");

                    username = jwtTokenUtil.getUsernameUnlimitedSkew(authToken);

                    final User user = userService.findByEmail(username).get();

                    UserSession userSession = userSessionRepository.findByUser(user);
                    userSession.setLoggedIn(0);
                    userSessionRepository.save(userSession);
                    username = null;
                } catch (final SignatureException e) {
                    logger.error("SignatureException. {}", e.getCause());
                }
            }
        } else {
            // logger.warn("couldn't find bearer string, will ignore the header");
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(authToken, userDetails)) {

                final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(req, res);
    }
}
