package com.javid.exception;

/**
 * @author javid
 * Created on 1/25/2022
 */
public class CardNumberException extends RuntimeException{

    public CardNumberException() {
    }

    public CardNumberException(String message) {
        super(message);
    }

    public CardNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}
