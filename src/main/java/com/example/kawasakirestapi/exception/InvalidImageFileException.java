package com.example.kawasakirestapi.exception;

public class InvalidImageFileException extends RuntimeException {
    private static final long serialVersionUID = 3533793618112345182L;

    public InvalidImageFileException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidImageFileException(String msg) {
        super(msg);
    }

}
