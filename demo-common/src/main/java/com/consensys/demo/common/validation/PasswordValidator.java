package com.consensys.demo.common.validation;

/**
 * Created by Calvin Ngo on 13/2/18.
 */
public class PasswordValidator implements FieldValidator {

    @Override
    public void validate(String value) throws InvalidValueException {
        if(value == null) {
            throw new InvalidValueException("Password cannot be null");
        }
        if(value.length() < 8) {
            throw new InvalidValueException("Password has to be at least 8 characters long");
        }
    }
}
