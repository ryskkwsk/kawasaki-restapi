package com.example.kawasakirestapi.application.exception;

public class InvalidAuthorizeException extends RuntimeException {

    public InvalidAuthorizeException(String msg) {
        super(msg);
    }

}
