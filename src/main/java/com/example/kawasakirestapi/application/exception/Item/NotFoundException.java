package com.example.kawasakirestapi.application.exception.Item;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundException extends RuntimeException{
    private static final long serialVersionUID = -2631315510426530295L;

    public NotFoundException(String msg) {
        super(msg);
    }
}
