package com.consensys.demo.common.messaging;

/**
 * Created by Calvin Ngo on 15/2/18.
 */
public class ContentMessage {

    private String contentId;
    private String contentType;
    private Exception error;

    public ContentMessage() {

    }

    public ContentMessage(String contentId, String contentType) {
        this.contentId = contentId;
        this.contentType = contentType;
    }

    public ContentMessage(String contentId, Exception error) {
        this.contentId = contentId;
        this.error = error;
    }

    public String getContentId() {
        return contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public Exception getError() {
        return error;
    }
}
