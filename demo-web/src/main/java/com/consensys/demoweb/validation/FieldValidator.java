package com.consensys.demoweb.validation;

/**
 * Created by Calvin Ngo on 13/2/18.
 */
public interface FieldValidator {

    void validate(String value) throws InvalidValueException;
}
