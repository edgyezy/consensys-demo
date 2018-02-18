package com.consensys.demo.pdf;

import com.consensys.demo.common.content.ContentStore;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class ITextPdfManager {
	private static Logger log = LoggerFactory.getLogger(ITextPdfManager.class);
	
	private static float PAGE_BORDER_PADDING = 10f;

	@Value("${files.masterPdfId}")
	private String masterPdfContentId;

    @Autowired
    private ContentStore contentStore;

	@Autowired
    private JmsTemplate jmsTemplate;

	public void createDocumentPdf(String contentId, String contentType) {
	    try {
            Path imagePath = contentStore.getFilePath(contentId, contentType);
            Image image = Image.getInstance(imagePath.toAbsolutePath().toString());
            
            float maxWidth = PageSize.A4.getWidth() - 2f*PAGE_BORDER_PADDING;
            float maxHeight = PageSize.A4.getHeight() - 2f*PAGE_BORDER_PADDING;
            
            if(image.getWidth() > maxWidth || image.getHeight() > maxHeight) {
            		image.scaleToFit(maxWidth, maxHeight);
            		image.setAbsolutePosition(PAGE_BORDER_PADDING, maxHeight - image.getScaledHeight());
            } else {
            		image.setAbsolutePosition(PAGE_BORDER_PADDING, maxHeight - image.getHeight());
            }
            

            Document document = new Document();

            File pdfFile = contentStore.newContentFile("application/pdf");
            String pdfContentID = pdfFile.getName();
            if(pdfContentID.lastIndexOf(".") > -1) {
                pdfContentID = pdfContentID.substring(0, pdfContentID.lastIndexOf('.'));
            }

            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            document.add(image);
            document.close();

            jmsTemplate.convertAndSend("collate_pdf", pdfContentID);
        } catch(DocumentException | IOException e) {
	        log.error("Failed to convert image to pdf", e);
        }
	}

	public void appendPdfToMaster(String contentId) {
	    try {
            Document document = new Document();

            File imagePdf = contentStore.getFilePath(contentId, "application/pdf").toFile();

            PdfReader imagePdfReader = new PdfReader(new FileInputStream(imagePdf));
            File masterPdf = contentStore.getFilePath(masterPdfContentId, "application/pdf").toFile();

            if(masterPdf.exists()) {
                File tempPdf = contentStore.newContentFile("application/pdf");
                PdfReader masterPdfReader = new PdfReader(new FileInputStream(masterPdf));
                PdfCopy copy = new PdfCopy(document, new FileOutputStream(tempPdf));
                document.open();
                for(int i=0; i < masterPdfReader.getNumberOfPages();) {
                    copy.addPage(copy.getImportedPage(masterPdfReader, ++i));
                }
                copy.addPage(copy.getImportedPage(imagePdfReader, 1));
                document.close();

                // Copy the write master to read master, overriding the old read master
                // This is done to prevent race condition where someone is viewing the master
                // and the PdfWriter is attempting to write to it.
                Files.copy(tempPdf.toPath(), masterPdf.toPath(), StandardCopyOption.REPLACE_EXISTING);
                tempPdf.delete();
            } else {
                Files.copy(imagePdf.toPath(), masterPdf.toPath());
            }
        } catch(DocumentException | IOException e) {
	        log.error("Failed to collate pdf", e);
        }

	}
}
