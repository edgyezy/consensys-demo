package com.consensys.demo.web.auth;

import com.consensys.demo.common.validation.InvalidValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Calvin Ngo on 13/2/18.
 */

@RestController
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserManagementService userManagementService;


    @PostMapping("/api/login")
    @Transactional
    public ResponseEntity login(HttpServletResponse response, Authentication auth) {
        String token = tokenProvider.createToken(auth);

        Cookie cookie = new Cookie("session_token", token);
        cookie.setPath("/");
        response.addCookie(cookie);

        return createAuthenticationResponse(auth.getName(), token);
    }

    @Transactional
    @PostMapping("/api/register")
    public ResponseEntity register(@RequestBody CreateUserRequest request, HttpServletResponse response)
        throws InvalidValueException, DuplicateAccountException {

        Account newAccount = userManagementService.createUser(request.getUsername(), request.getPassword());
        String token = tokenProvider.createToken(newAccount);

        Cookie cookie = new Cookie("session_token", token);
        cookie.setPath("/");
        response.addCookie(cookie);

        return createAuthenticationResponse(request.getUsername(), token);
    }

    @ExceptionHandler(DuplicateAccountException.class)
    public ResponseEntity handleDuplicateAccount() {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "An account with this name already exists");
        return new ResponseEntity(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity handleInvalidCredentials(InvalidValueException e) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity(responseBody, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity createAuthenticationResponse(String username, String token) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        if(token != null) {
            headers.put("session_token", Arrays.asList(token));
            headers.put("Location", Arrays.asList("/main/"));
        }

        // This should return a 302, but fetch api client side cannot see location header for 3xx, return 200 instead
        return new ResponseEntity(headers, HttpStatus.OK);
    }
}
