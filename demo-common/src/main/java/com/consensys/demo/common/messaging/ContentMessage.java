package com.consensys.demo.common.messaging;

/**
 * Created by Calvin Ngo on 15/2/18.
 */
public class ContentMessage {

    public enum Action { CREATED, UPDATED, DELETED}

    private String contentId;
    private Exception error;

    public ContentMessage(Action action, String contentId) {
        this.contentId = contentId;
    }

    public ContentMessage(String contentId, Exception error) {
        this.contentId = contentId;
        this.error = error;
    }

    public String getContentId() {
        return contentId;
    }

    public Exception getError() {
        return error;
    }
}
