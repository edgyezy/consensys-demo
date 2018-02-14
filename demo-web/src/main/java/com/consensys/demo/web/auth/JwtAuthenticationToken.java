package com.consensys.demo.web.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Created by Calvin Ngo on 13/2/18.
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private Account principal;
    private String key;

    public JwtAuthenticationToken(Account principal, String key) {
        super(principal.getAuthorities());
        this.principal = principal;
        this.key = key;
    }

    @Override
    public Object getCredentials() {
        return key;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
