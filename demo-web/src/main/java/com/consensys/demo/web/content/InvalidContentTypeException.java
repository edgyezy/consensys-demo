package com.consensys.demo.web.content;

/**
 * Created byCalvin Ngo on 14/2/18.
 */
public class InvalidContentTypeException extends RuntimeException {

    private String message;
    private String contentType;

    public InvalidContentTypeException(String contentType, String message) {
        this.contentType = contentType;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getContentType() {
        return contentType;
    }
}
