package com.example.kawasakirestapi.exception;

public class InvalidImageFileException extends RuntimeException {
    private static final long serialVersionUID = 3533793618112345182L;

    public InvalidImageFileException(String msg, Exception e) {
        super(msg, e);
    }

    public InvalidImageFileException(String msg) {
        super(msg);
    }

}
