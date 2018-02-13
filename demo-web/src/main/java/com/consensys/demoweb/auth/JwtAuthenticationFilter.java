package com.consensys.demoweb.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Calvin Ngo on 13/2/18.
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

   public static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        if (StringUtils.isEmpty(token) || token.equals("null")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if(isAuthenticationRequired()) {
                if (this.tokenProvider.validateToken(token)) {
                    Authentication authRequest = tokenProvider.getAuthentication(token);
                    Authentication authResult = this.authenticationManager.authenticate(authRequest);
                    SecurityContextHolder.getContext().setAuthentication(authResult);
                } else {
                    Cookie cookie = new Cookie("session_token", null);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    response.setStatus(401);
                }
            }
        } catch(AuthenticationException e) {
            SecurityContextHolder.clearContext();
        }
        finally {
            filterChain.doFilter(request, response);
        }
    }

    private boolean isAuthenticationRequired() {
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
        if(existingAuth == null || !existingAuth.isAuthenticated()) {
            return true;
        }
        if(existingAuth instanceof JwtAuthenticationToken && !tokenProvider.validateToken((String)existingAuth.getCredentials())) {
            return true;
        }
        if(existingAuth instanceof AnonymousAuthenticationToken) {
            return true;
        }
        return false;
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if(token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        } else {
            if(request.getCookies() != null) {
                for (Cookie c : request.getCookies()) {
                    if ("session_token".equals(c.getName())) {
                        return c.getValue();
                    }
                }
            }
        }
        return null;
    }
}
