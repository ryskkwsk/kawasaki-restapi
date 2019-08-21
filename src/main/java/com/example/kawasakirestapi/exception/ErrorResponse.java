package com.example.kawasakirestapi.exception;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private final Error error;

    private HttpStatus status;

    private String message;

    public ErrorResponse(HttpStatus status, String message) {
        error = new Error(status, message);
    }

    private class Error {
        private final String message;

        private final HttpStatus status;

        Error(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }
    }

}
