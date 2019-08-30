package com.example.kawasakirestapi.application.exception.oauth;

import lombok.extern.slf4j.Slf4j;

/**
 * トークンの有効期限が切れている時に返す例外クラス
 *
 * @author kawasakiryosuke
 */
@Slf4j
public class TokenTimeoutException extends RuntimeException {

    public TokenTimeoutException() {
        super("トークンの有効期限が切れています。");
    }
}
