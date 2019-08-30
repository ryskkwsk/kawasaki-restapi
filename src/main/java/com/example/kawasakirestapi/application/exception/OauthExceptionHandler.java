package com.example.kawasakirestapi.application.exception;

import com.example.kawasakirestapi.application.exception.oauth.InvalidAuthorizeException;
import com.example.kawasakirestapi.application.exception.oauth.TokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * ソーシャルログイン認証のOAuthコントローラーにかかる例外ハンドラ
 *
 * @author kawasakiryosuke
 */
@RestControllerAdvice
@Slf4j
public class OauthExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     *  ユーザー認証がうまくいかなかった時にエラーを返す
     * @param ex
     * @return 401エラーページを返す
     */
    @ExceptionHandler(InvalidAuthorizeException.class)
    public String handleInvalidAuthorizeException(InvalidAuthorizeException ex) {
        log.warn(ex.getMessage(), ex);
        return "/error/401";
    }

    /**
     * リクエストにtokenが存在しない場合にエラーを返す
     *
     * @param ex      例外
     * @param request リクエスト
     * @return 401エラー情報を返す
     */
    @ExceptionHandler({ TokenNotFoundException.class })
    public ResponseEntity<Object> tokenNotFoundException(
            TokenNotFoundException ex, WebRequest request) {

        log.warn(ex.getMessage(), ex);
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse body = new ErrorResponse(status, ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, request);
    }


}
