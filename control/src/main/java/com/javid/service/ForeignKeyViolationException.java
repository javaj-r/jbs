package com.javid.service;

/**
 * @author javid
 * Created on 1/24/2022
 */
public class ForeignKeyViolationException extends RuntimeException {

    public ForeignKeyViolationException() {
    }

    public ForeignKeyViolationException(String message) {
        super(message);
    }

    public ForeignKeyViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
