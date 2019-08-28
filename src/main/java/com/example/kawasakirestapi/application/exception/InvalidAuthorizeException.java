package com.example.kawasakirestapi.application.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * ユーザー情報の認証が失敗した時に返す例外クラス
 */
@Slf4j
public class InvalidAuthorizeException extends RuntimeException {

    public InvalidAuthorizeException(String msg) {
        super(msg);
    }

}
