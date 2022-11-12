package com.natujenge.thecouch.security;

import com.natujenge.thecouch.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

import static com.natujenge.thecouch.domain.Constants.ACCESS_TOKEN_VALIDITY_SECONDS;
import static com.natujenge.thecouch.domain.Constants.SIGNING_KEY;

@Component
@Slf4j
public class JwtTokenUtil implements Serializable {
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Integer getCoachIdFromToken(String token) {
        return (Integer) getAllClaimsFromToken(token).get("cid");
    }

    public ArrayList getRolesFromToken(String token) {
        return (ArrayList) getAllClaimsFromToken(token).get("scopes");
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        try{
            return Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token).getBody();
        } catch (Exception e){
            log.error("{}", e.getMessage());
            return null;
        }
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(User user) {
        return doGenerateToken(user);
    }

    private String doGenerateToken(User user) {

        final Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority(user.getUserRole().name())));
        claims.put("cid", user.getCoach() != null ? user.getCoach().getId(): null);

        return Jwts.builder().setClaims(claims).setIssuer("https://natujenge.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String getUsername(String token) {
        final String username = Jwts.parser().setSigningKey(SIGNING_KEY).setAllowedClockSkewSeconds(5 * 60 * 60)
                .parseClaimsJws(token).getBody().getSubject();
        try {
            Jwts.parser().setSigningKey(SIGNING_KEY).setAllowedClockSkewSeconds(0).parseClaimsJws(token).getBody()
                    .getSubject();
        } catch (final Exception e) {

        }
        return username;
    }

    public String getUsernameUnlimitedSkew(String token) {
        String username=null;
        try {
            username = Jwts.parser().setSigningKey(SIGNING_KEY).setAllowedClockSkewSeconds(Integer.MAX_VALUE)
                    .parseClaimsJws(token).getBody().getSubject();
        } catch (final Exception e) {
            System.out.println(e.getMessage());
        }
        return username;
    }
}
