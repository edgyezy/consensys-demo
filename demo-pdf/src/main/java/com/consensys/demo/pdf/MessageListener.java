package com.consensys.demo.pdf;

import com.consensys.demo.common.content.ContentStore;
import com.consensys.demo.common.messaging.ContentMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.nio.file.Path;


/**
 * Created by Calvin Ngo on 15/2/18.
 */

@Component
public class MessageListener {
    private static Logger log = LoggerFactory.getLogger(MessageListener.class);

    @Autowired
    private PdfManager pdfManager;

    @JmsListener(destination = "convert_image")
    public void receiveImageUploadedMessage(ContentMessage message) {
        log.info("image received " + message.getContentId());
        pdfManager.createDocumentPdf(message.getContentId(), message.getContentType());
    }
    
    @JmsListener(destination = "collate_pdf")
    public void receivePdfConversionMessage(String pdfFragment) {
        pdfManager.appendPdfToMaster(pdfFragment);
    }
    
}
