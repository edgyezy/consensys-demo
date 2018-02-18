package com.consensys.demo.common.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Created by Calvin Ngo on 13/2/18.
 */

@Repository
public class ContentStore {

    private static final Logger log = LoggerFactory.getLogger(ContentStore.class);

    @Value("${files.imageLocation}")
    private String imageRootPath;
    private File imageRoot;

    @PostConstruct
    public void init() throws IOException {
        imageRoot = new File(imageRootPath);
        if(!imageRoot.exists()) {
            log.info("Image root " + imageRoot.getAbsolutePath() + " does not exist, creating directory.");
            try {
                imageRoot.mkdirs();
            } catch(SecurityException e) {
                log.error("Failed to create image root", e);
            }
        }
        if(!imageRoot.canRead() || !imageRoot.canWrite()) {
            throw new IOException("Read/write permissions missing for " + imageRootPath);
        }
    }

    public boolean exists(String contentId) {
        return new File(imageRoot, contentId).exists();
    }

    public Path getFilePath(String contentId, String contentType) {
        return Paths.get(imageRoot.getAbsolutePath(), contentId + getFileExtension(contentType));
    }

    public File newContentFile(String contentType) {
        String contentFileName = UUID.randomUUID().toString() + getFileExtension(contentType);
        File contentFile = new File(imageRoot, contentFileName);
        while (contentFile.exists()) {
            contentFileName = UUID.randomUUID().toString() + getFileExtension(contentType);
            contentFile = new File(imageRoot, contentFileName);
        }
        return contentFile;
    }

    private String getFileExtension(String contentType) {
        switch(contentType) {
            case "image/jpeg" : return ".jpg";
            case "image/png" : return ".png";
            default : return "";
        }
    }
}
