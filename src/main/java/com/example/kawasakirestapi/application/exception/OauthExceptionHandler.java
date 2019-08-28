package com.example.kawasakirestapi.application.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * ソーシャルログイン認証のOAuthコントローラーにかかる例外ハンドラ
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


}
