package com.example.kawasakirestapi.application.exception;

import lombok.extern.slf4j.Slf4j;

/**
 *  商品情報が見つからなかった時に返す例外クラス
 *
 *  @author kawasakiryosuke
 */
@Slf4j
public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(String msg) {
        super(msg);
    }

}
