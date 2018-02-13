package com.consensys.demoweb.validation;

/**
 * Created by Calvin Ngo on 13/2/18.
 */
public class InvalidValueException extends RuntimeException {

    private String message;

    public InvalidValueException(String message) {
        this.message = message;
    }

    public final String getMessage() {
        return message;
    }
}
