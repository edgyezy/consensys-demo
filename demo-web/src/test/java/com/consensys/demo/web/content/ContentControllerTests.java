package com.consensys.demo.web.content;

import com.consensys.demo.common.content.ContentStore;
import com.consensys.demo.web.auth.Account;
import com.consensys.demo.web.auth.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created byCalvin Ngo on 14/2/18.
 */

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ContentControllerTests {

    @Autowired
    private ContentController controller;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ContentStore store;

    @Autowired
    private ContentIndexRepository indexRepository;

    private Account account;

    @Before
    public void setup() {
        account = accountRepository.findByUsername("test@example.org");
    }

    @Test
    public void shouldProcessFileUpload() throws Exception {
        File testFile = new File(getClass().getResource("/cat.jpg").toURI());

        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn(MediaType.IMAGE_JPEG_VALUE);
        when(file.getInputStream()).thenReturn(new FileInputStream(testFile));
        when(file.getName()).thenReturn("cat.jpg");

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(account);
        when(auth.isAuthenticated()).thenReturn(true);

        controller.acceptImage(file, auth);

        List<UserContent> result = indexRepository.findByOwner(account);
        assertEquals(1, result.size());

        UserContent savedContent = result.get(0);
        assertEquals("cat.jpg", savedContent.getFileName());
        assertEquals(MediaType.IMAGE_JPEG_VALUE, savedContent.getContentType());
        assertEquals(testFile.length(), store.getFilePath(savedContent.getContentId(), MediaType.IMAGE_JPEG_VALUE).toFile().length());
    }

    @Test
    public void shouldRejectNonImageFiles() throws Exception {
        File testFile = new File(getClass().getResource("/not-cat.txt").toURI());

        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn(MediaType.TEXT_PLAIN_VALUE);
        when(file.getInputStream()).thenReturn(new FileInputStream(testFile));
        when(file.getName()).thenReturn("not-cat.txt");

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(account);
        when(auth.isAuthenticated()).thenReturn(true);

        ResponseEntity response = controller.acceptImage(file, auth);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad content type, this endpoint only accepts JPEG and PNG images.", response.getBody());
    }

}