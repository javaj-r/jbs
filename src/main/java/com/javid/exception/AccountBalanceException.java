package com.javid.exception;

/**
 * @author javid
 * Created on 1/23/2022
 */
public class AccountBalanceException extends RuntimeException {

    public AccountBalanceException() {
    }

    public AccountBalanceException(String message) {
        super(message);
    }

    public AccountBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
