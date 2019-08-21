package com.example.kawasakirestapi.exception;

public class ItemNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3533793618112345182L;

    public ItemNotFoundException(String msg) {
        super(msg);
    }

}
