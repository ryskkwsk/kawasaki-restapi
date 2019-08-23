package com.example.kawasakirestapi.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {

    private final Error error;

    public ErrorResponse(HttpStatus status, String message) {
        error = new Error(status, message);
    }
    @Data
    private class Error {
        private final String message;
        private final HttpStatus status;

        Error(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }
    }


}
