package com.consensys.demo.pdf;

import com.consensys.demo.common.messaging.ContentMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

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
}
