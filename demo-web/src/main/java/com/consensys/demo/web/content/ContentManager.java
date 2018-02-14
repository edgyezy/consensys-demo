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
    public UserContent createContent(Account owner, String contentType, String fileName) {
        String contentFileName = null;
        File contentFile = contentStore.newContentFile(contentType);
        contentFileName = contentFile.getName();

        UserContent userContent = new UserContent();
        userContent.setContentType(contentType);
        userContent.setFileName(fileName);
        userContent.setContentId(contentFileName);
        userContent.setOwner(owner);
        userContent.setUploadStatus(UserContent.UploadStatus.IN_PROGRESS);

        userContent = contentIndexRepository.save(userContent);
        return userContent;
    }

    @Transactional
    public void cancelContent(UserContent userContent) {
        if(userContent != null) {
            userContent.setUploadStatus(UserContent.UploadStatus.CANCELLED);
            contentIndexRepository.save(userContent);
        }
    }

    public void read(String contentId, OutputStream outputStream) throws IOException {
        Files.copy(contentStore.getFilePath(contentId), outputStream);
    }

    @Transactional
    public void write(InputStream inputStream, UserContent content) throws IOException {
        Files.copy(inputStream, contentStore.getFilePath(content.getContentId()));
        content.setUploadStatus(UserContent.UploadStatus.FINISHED);
        contentIndexRepository.save(content);
    }
}
