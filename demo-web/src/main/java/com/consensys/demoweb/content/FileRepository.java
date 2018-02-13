package com.consensys.demoweb.content;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Calvin Ngo on 13/2/18.
 */

@Repository
public class FileRepository {

    @Value("${files.pdfLocation}")
    private String pdfRootPath;
    private File pdfRoot;

    @Value("${files.imageLocation}")
    private String imageRootPath;
    private File imageRoot;

    @PostConstruct
    public void init() throws IOException {
        pdfRoot = new File(pdfRootPath);
        if(!pdfRoot.exists()) {
            throw new IOException("PDF location " + pdfRootPath + " does not exist");
        }
        if(!pdfRoot.canRead() || !pdfRoot.canWrite()) {
            throw new IOException("Read/write permissions missing for " + pdfRootPath);
        }

        imageRoot = new File(imageRootPath);
        if(!imageRoot.exists()) {
            throw new IOException("Image location " + imageRootPath + " does not exist");
        }
        if(!imageRoot.canRead() || !imageRoot.canWrite()) {
            throw new IOException("Read/write permissions missing for " + imageRootPath);
        }
    }

    public File createJpgImage(File directory) {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        File contentFile = new File(fileName, imageRoot.getAbsolutePath());
        return contentFile;
    }

    public File createPngImage(File directory) {
        String fileName = UUID.randomUUID().toString() + ".png";
        File contentFile = new File(fileName, imageRoot.getAbsolutePath());
        return contentFile;
    }

    public File createPdf(File directory) {
        String fileName = UUID.randomUUID().toString() + ".pdf";
        File contentFile = new File(fileName, imageRoot.getAbsolutePath());
        return contentFile;
    }
}
