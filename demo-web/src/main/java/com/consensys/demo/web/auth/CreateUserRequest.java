package com.consensys.demo.web.auth;

/**
 * Created by Calvin Ngo on 13/2/18.
 */
public class CreateUserRequest {

    private String username;
    private String password;

    public CreateUserRequest() {}

    public CreateUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
