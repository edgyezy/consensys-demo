package com.consensys.demo.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.consensys.demo.common.messaging.ContentMessage;


/**
 * Created by Calvin Ngo on 15/2/18.
 */

@Component
public class ImageListener {
    private static Logger log = LoggerFactory.getLogger(ImageListener.class);
    
    
    @JmsListener(destination = "images")
    public void receiveImageUploadedMessage(ContentMessage message) {
        log.info("image received " + message.getContentId());
    }
    
    @JmsListener(destination = "collatepdf")
    public void receivePdfConversionMessage(ContentMessage message) {
    		log.info("pdf conversion received " + message.getContentId());
    }
    
}
