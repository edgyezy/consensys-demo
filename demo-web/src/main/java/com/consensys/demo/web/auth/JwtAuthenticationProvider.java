package com.consensys.demo.web.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by Calvin Ngo on 13/2/18.
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication == null || !supports(authentication.getClass())) {
            return null;
        }
        Account account = (Account)authentication.getPrincipal();
        if(account != null && account.isEnabled() && account.isAccountNonLocked()) {
            authentication.setAuthenticated(true);
        }
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}

