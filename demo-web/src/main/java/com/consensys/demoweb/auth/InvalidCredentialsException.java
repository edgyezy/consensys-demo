package com.consensys.demoweb.auth;

/**
 * Created by Calvin Ngo on 13/2/18.
 */
public class InvalidCredentialsException extends Exception {

    private String message;

    public InvalidCredentialsException(String message) {
        this.message = message;
    }

    public final String getMessage() {
        return message;
    }
}
