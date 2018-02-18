package com.consensys.demo.web.content;

import com.consensys.demo.common.content.ContentStore;
import com.consensys.demo.common.messaging.ContentMessage;
import com.consensys.demo.web.auth.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * Created by Calvin Ngo on 14/2/18.
 */

@RestController
public class ContentController {

    private static final Logger log = LoggerFactory.getLogger(ContentController.class);

    @Value("${files.masterPdfId}")
    private String masterPdfContentId;

    @Autowired
    private ContentManager contentManager;

    @Autowired
    private ContentStore contentStore;

    @Autowired
    private ContentIndexRepository contentIndexRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    private static File blankPdf;

    public ContentController() {
        try {
            blankPdf = new File(getClass().getResource("/blank.pdf").toURI());
        } catch(NullPointerException | URISyntaxException e) {
            log.error("Failed to load default blank PDF", e);
        }
    }

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
            String contentId = contentManager.write(file.getInputStream(), contentType);
            UserContent content = contentManager.createContent(account, contentType, contentId, file.getName(), file.getSize());

            jmsTemplate.convertAndSend("convert_image", new ContentMessage(content.getContentId(), contentType));
            return new ResponseEntity<>(new ContentDTO(content), HttpStatus.OK);
        } catch(IOException | JmsException e) {
            log.error("Failed to handle content upload", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/content/images.pdf")
    public void getPdf(HttpServletResponse response) {
        log.info("Fetching PDF");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment;filename='images.pdf'");
        try {
            File pdfFile = contentStore.getFilePath(masterPdfContentId, "application/pdf").toFile();
            if(!pdfFile.exists()) {
                pdfFile = blankPdf;
            }
            response.setContentLengthLong(pdfFile.length());
            Files.copy(pdfFile.toPath(), response.getOutputStream());
            response.flushBuffer();
            response.setStatus(200);
        } catch (IOException e) {
            log.error("Failed to send PDF file", e);
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
