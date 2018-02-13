package com.consensys.demoweb.validation;

/**
 * Created by Calvin Ngo on 13/2/18.
 */
public class UsernameValidator implements FieldValidator {

    @Override
    public void validate(String username) throws InvalidValueException {
        if(username == null) {
            throw new InvalidValueException("Username cannot be null");
        }
        if(!username.matches("^.+@.+\\..+")) {
            throw new InvalidValueException("Username must be a valid email address");
        }

    }
}
