package com.consensys.demo.common.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    @Value("${files.pdfLocation}")
    private String pdfRootPath;
    private File pdfRoot;

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
        } else {
        		Files.list(imageRoot.toPath()).map(p -> p.toFile()).forEach(p->p.delete());
        }
        
        if(!imageRoot.canRead() || !imageRoot.canWrite()) {
            throw new IOException("Read/write permissions missing for " + imageRootPath);
        }

        pdfRoot = new File(pdfRootPath);
        if(!pdfRoot.exists()) {
            log.info("PDF root " + pdfRoot.getAbsolutePath() + " does not exist, creating directory.");
            try {
                pdfRoot.mkdirs();
            } catch (SecurityException e) {
                log.error("Failed to create pdf root", e);
            }
        } else {
        		Files.list(pdfRoot.toPath()).map(p -> p.toFile()).forEach(p->p.delete());
        }
        if(!pdfRoot.canRead() || !imageRoot.canWrite()) {
            throw new IOException("Read/write permissions missing for " + pdfRootPath);
        }
    }

    public Path getFilePath(String contentId, String contentType) {
        File contentRoot = imageRoot;
        if("application/pdf".equals(contentType)) {
            contentRoot = pdfRoot;
        }
        return (new File(contentRoot, contentId + getFileExtension(contentType))).toPath();
    }

    public File newContentFile(String contentType) throws IOException {
        File contentRoot = imageRoot;
        if("application/pdf".equals(contentType)) {
            contentRoot = pdfRoot;
        }
        String contentFileName = UUID.randomUUID().toString() + getFileExtension(contentType);
        File contentFile = new File(contentRoot, contentFileName);
        while (contentFile.exists()) {
            contentFileName = UUID.randomUUID().toString() + getFileExtension(contentType);
            contentFile = new File(contentRoot, contentFileName);
        }
        contentFile.createNewFile();
        return contentFile;
    }

    private String getFileExtension(String contentType) {
        switch(contentType) {
            case "image/jpeg" : return ".jpg";
            case "image/png" : return ".png";
            case "application/pdf" : return ".pdf";
            default : return "";
        }
    }
}
