package com.consensys.demoweb.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Calvin Ngo on 13/2/18.
 */

@Component
public class JwtTokenProvider {

    private static final Logger log = getLogger(JwtTokenProvider.class);
    private Key key = MacProvider.generateKey();

    @Autowired
    private UserDetailsService userDetailsService;

    public String createToken(Authentication authentication) {
        if(authentication == null ||
            authentication instanceof AnonymousAuthenticationToken ||
            !authentication.isAuthenticated()) {
            return null;
        }

        Account account = (Account)authentication.getPrincipal();
        return createToken(account);
    }

    public String createToken(Account account) {
        StringBuffer buf = new StringBuffer();
        for(GrantedAuthority authority : account.getAuthorities()) {
            buf.append(authority.getAuthority()).append(";");
        }

        JwtBuilder builder = Jwts.builder()
                .setSubject(account.getUsername())
                .signWith(SignatureAlgorithm.HS512, key);
        return builder.compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        Account principal = (Account)userDetailsService.loadUserByUsername(claims.getSubject());
        return new JwtAuthenticationToken(principal, token);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature: " + e.getMessage());
            return false;
        }
    }
}
