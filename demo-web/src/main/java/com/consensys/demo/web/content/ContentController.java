package com.consensys.demo.web.content;

import com.consensys.demo.common.messaging.ContentMessage;
import com.consensys.demo.web.auth.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import static com.consensys.demo.common.messaging.ContentMessage.Action.CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * Created by Calvin Ngo on 14/2/18.
 */

@RestController
public class ContentController {

    private static final Logger log = LoggerFactory.getLogger(ContentController.class);

    @Autowired
    private ContentManager contentManager;

    @Autowired
    private ContentIndexRepository contentIndexRepository;

    @Autowired
    private JmsTemplate jmsTemplate;


    @GetMapping("/api/images")
    public ResponseEntity<List<ContentDTO>> listImages(Authentication authentication) {
        Account account = null;
        if(authentication != null && authentication.isAuthenticated()) {
            account = (Account)authentication.getPrincipal();
        }

        List<ContentDTO> responseBody = contentIndexRepository.findByOwner(account).stream()
                .map(userContent -> new ContentDTO(userContent))
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping(value = "/api/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity acceptImage(@RequestParam("content") MultipartFile file, Authentication authentication) {
        Account account = null;
        if(authentication != null) {
            account = (Account)authentication.getPrincipal();
        }

        if(!authentication.isAuthenticated() || account == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String contentType = file.getContentType();
        if(!MediaType.IMAGE_JPEG_VALUE.equals(contentType) && !MediaType.IMAGE_PNG_VALUE.equals(contentType)) {
            return new ResponseEntity<>("Bad content type, this endpoint only accepts JPEG and PNG images.", HttpStatus.BAD_REQUEST);
        }

        try {
            File contentFile = contentManager.write(file.getInputStream(), contentType);
            UserContent content = contentManager.createContent(account, contentType, contentFile.getName(), file.getName(), file.getSize());

            jmsTemplate.convertAndSend("images", new ContentMessage(CREATED, content.getContentId()));
            return new ResponseEntity<>(new ContentDTO(content), HttpStatus.OK);
        } catch(IOException | JmsException e) {
            log.error("Failed to handle content upload", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/content/{contentId}")
    public void getContent(@PathVariable("contentId") String contentId, HttpServletResponse response) {
        try {
            UserContent content = contentIndexRepository.findByContentId(contentId);

            response.setContentType(content.getContentType());
            Long contentLength = content.getContentLength();
            if(contentLength != null) {
                response.setContentLengthLong(contentLength);
            }

            OutputStream outStream = response.getOutputStream();
            contentManager.read(contentId, outStream);
            outStream.flush();
            response.setStatus(SC_OK);

        } catch(FileNotFoundException e) {
            response.setStatus(SC_NOT_FOUND);
        } catch (IOException e) {
            log.error("Failed to fetch content " + contentId, e);
        }
    }
}
