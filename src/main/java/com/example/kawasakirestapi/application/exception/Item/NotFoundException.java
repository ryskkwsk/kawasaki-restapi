package com.example.kawasakirestapi.application.exception.Item;

public class NotFoundException extends RuntimeException{
    private static final long serialVersionUID = -2631315510426530295L;

    public NotFoundException(String msg) {
        super(msg);
    }
}
