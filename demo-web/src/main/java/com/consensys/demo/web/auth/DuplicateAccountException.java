package com.consensys.demo.web.auth;

/**
 * Created by Calvin Ngo on 13/2/18.
 */
public class DuplicateAccountException extends Exception {

    private String username;

    public DuplicateAccountException(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String toString() {
        return username + " already exists.";
    }
}
