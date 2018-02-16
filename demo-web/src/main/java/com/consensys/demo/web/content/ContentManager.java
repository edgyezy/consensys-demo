package com.consensys.demo.web.content;

import com.consensys.demo.common.content.ContentStore;
import com.consensys.demo.web.auth.Account;
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

    @Autowired
    private ContentIndexRepository contentIndexRepository;

    @Autowired
    private ContentStore contentStore;

    @Transactional
    public UserContent createContent(Account owner, String contentType, String contentFileName, String fileName, Long size) {
        UserContent userContent = new UserContent();
        userContent.setContentType(contentType);
        userContent.setContentLength(size);
        userContent.setFileName(fileName);
        userContent.setContentId(contentFileName);
        userContent.setOwner(owner);
        userContent = contentIndexRepository.save(userContent);
        return userContent;
    }

    public void read(String contentId, OutputStream outputStream) throws IOException {
        Files.copy(contentStore.getFilePath(contentId), outputStream);
    }

    @Transactional
    public File write(InputStream inputStream, String contentType) throws IOException {
        File contentFile = contentStore.newContentFile(contentType);
        Files.copy(inputStream, contentFile.toPath());
        return contentFile;
    }
}
