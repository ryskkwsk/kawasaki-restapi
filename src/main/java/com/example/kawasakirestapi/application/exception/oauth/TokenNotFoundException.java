package com.example.kawasakirestapi.application.exception.oauth;

import lombok.extern.slf4j.Slf4j;

/**
 * トークンが見つからなかった時に返す例外クラス
 *
 * @author kawasakiryosuke
 */
@Slf4j
public class TokenNotFoundException extends RuntimeException {

    public TokenNotFoundException(String msg) {
        super(msg);
    }

}
