package com.example.kawasakirestapi.exception;

public class ImageNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3533793618112345182L;

    public ImageNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
