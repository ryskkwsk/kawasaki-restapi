package com.example.kawasakirestapi.application.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 *  エラー発生時のレスポンス
 *
 * @author kawasakiryosuke
 */
@Data
public class ErrorResponse {

    // エラーの詳細メッセージ
    private final Error error;

    public ErrorResponse(HttpStatus status, String message) {
        error = new Error(status, message);
    }
    @Data
    private class Error {
        // エラーの概要メッセージ
        private final String message;
        // Httpステータス
        private final HttpStatus status;

        Error(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }
    }


}
