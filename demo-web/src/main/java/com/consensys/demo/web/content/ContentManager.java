package com.consensys.demo.web.content;

import com.consensys.demo.common.content.ContentStore;
import com.consensys.demo.web.auth.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by Calvin Ngo on 14/2/18.
 */

@Service
public class ContentManager {

    private static final Logger log = LoggerFactory.getLogger(ContentManager.class);

    @Autowired
    private ContentIndexRepository contentIndexRepository;

    @Autowired
    private ContentStore contentStore;


    @Transactional
    public UserContent createContent(Account owner, String contentType, String contentId, String fileName, Long size) {
        UserContent userContent = new UserContent();
        userContent.setContentType(contentType);
        userContent.setContentLength(size);
        userContent.setFileName(fileName);
        userContent.setContentId(contentId);
        userContent.setOwner(owner);
        userContent = contentIndexRepository.save(userContent);
        return userContent;
    }

    public void read(String contentId, OutputStream outputStream) throws IOException {
        UserContent content = contentIndexRepository.findByContentId(contentId);
        if(content != null) {
            Files.copy(contentStore.getFilePath(contentId, content.getContentType()), outputStream);
        }
    }

    @Transactional
    public String write(InputStream inputStream, String contentType) throws IOException {
        File contentFile = contentStore.newContentFile(contentType);
        Files.copy(inputStream, contentFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        String contentId = contentFile.getName();
        if(contentId.lastIndexOf(".") > -1) {
            contentId = contentId.substring(0, contentId.lastIndexOf('.'));
        }
        return contentId;
    }
}
