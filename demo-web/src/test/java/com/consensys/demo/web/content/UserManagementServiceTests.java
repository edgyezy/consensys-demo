package com.consensys.demo.web.content;

import com.consensys.demo.web.auth.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Calvin Ngo on 13/2/18.
 */

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserManagementServiceTests {

    @Value("${auth.defaultUser.username}")
    private String defaultUsername;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthenticationController controller;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    public void shouldHaveDefaultUser() {
        Account adminAccount = accountRepository.findByUsername(defaultUsername);
        assertEquals(defaultUsername, adminAccount.getUsername());
    }

    @Test
    @Transactional
    public void shouldCreateUser() {
        String username = "testUser@example.org";
        String password = "welcome321";
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        try {
            controller.register(new CreateUserRequest(username, password), response);
            Account account = accountRepository.findByUsername(username);
            assertNotNull(account);
            assertTrue(passwordEncoder.matches(password, account.getPassword()));
        } catch(DuplicateAccountException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = DuplicateAccountException.class)
    @Transactional
    public void shouldCauseDuplicateAccountException() throws Exception {
        String username = "test@example.org";
        String password = "welcome321";
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        controller.register(new CreateUserRequest(username, password), response);
    }

}
