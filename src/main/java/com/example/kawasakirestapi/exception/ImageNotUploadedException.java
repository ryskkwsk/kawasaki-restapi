package com.example.kawasakirestapi.exception;

public class ImageNotUploadedException extends RuntimeException {
    private static final long serialVersionUID = 3533793618112345182L;

    public ImageNotUploadedException (String msg, Throwable cause) {
        super(msg, cause);
    }

}
