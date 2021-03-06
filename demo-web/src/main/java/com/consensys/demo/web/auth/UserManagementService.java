package com.consensys.demo.web.auth;

import com.consensys.demo.common.validation.InvalidValueException;
import com.consensys.demo.common.validation.UsernameValidator;
import com.consensys.demo.common.validation.PasswordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;

/**
 * Created by Calvin Ngo on 13/2/18.
 */

@Service
public class UserManagementService {

    private static final Logger log = LoggerFactory.getLogger(UserManagementService.class);

    private SecureRandom secureRandom = new SecureRandom();

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsernameValidator usernameValidator;
    @Autowired
    private PasswordValidator passwordValidator;

    @Value("${auth.defaultUser.username}")
    private String defaultUsername;
    @Value("${auth.defaultUser.password}")
    private String defaultUserPassword;

    @PostConstruct
    @Transactional
    public void initialize() {
        createDefaultUser();
    }

    @Transactional
    protected void createDefaultUser() {
        if(defaultUsername != null && defaultUserPassword != null) {
            Account defaultAdmin = accountRepository.findByUsername(defaultUsername);
            if(defaultAdmin == null) {
                try {
                    createUser(defaultUsername, defaultUserPassword);
                } catch(DuplicateAccountException e) {
                    // do nothing
                }
            }
            log.info("Default user account is " + defaultUsername);
        } else {
            log.info("No default user account created.");
        }
    }

    @Transactional
    public Account createUser(String username, String password)
            throws DuplicateAccountException, InvalidValueException {
        usernameValidator.validate(username);
        passwordValidator.validate(password);

        Account account = accountRepository.findByUsername(username);
        if(account != null) {
            throw new DuplicateAccountException(username);
        }

        account = new Account();
        account.setUsername(username);
        passwordValidator.validate(password);
        account.setPassword(passwordEncoder.encode(password));
        account = accountRepository.save(account);

        return account;
    }
}
