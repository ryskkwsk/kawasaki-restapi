package com.example.kawasakirestapi.application.exception.Item;

import lombok.extern.slf4j.Slf4j;

/**
 * 対象の商品画像が見つからなかった時に返す例外クラス
 *
 * @author kawasakiryosuke
 */
@Slf4j
public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
