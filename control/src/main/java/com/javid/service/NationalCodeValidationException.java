package com.javid.service;

/**
 * @author javid
 * Created on 1/23/2022
 */
public class NationalCodeValidationException extends RuntimeException {

    public NationalCodeValidationException() {
    }

    public NationalCodeValidationException(String message) {
        super(message);
    }

    public NationalCodeValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
