package com.example.kawasakirestapi.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

public class ErrorResponse {
    @JsonProperty("Error")
    private final Error error;

    private HttpStatus status;

    private String message;

    public ErrorResponse(HttpStatus status, String message) {
        error = new Error(status, message);
    }

    private class Error {
        @JsonProperty("message")
        private final String message;

        @JsonProperty("status")
        private final HttpStatus status;

        Error(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }
    }

}
