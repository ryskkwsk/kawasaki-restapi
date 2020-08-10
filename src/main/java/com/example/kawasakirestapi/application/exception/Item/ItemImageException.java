package com.example.kawasakirestapi.application.exception.Item;

import lombok.extern.slf4j.Slf4j;

/**
 *  画像の投稿・取得・削除・アップロードに失敗した時に返す例外クラス
 *
 *  @author kawasakiryosuke
 */
@Slf4j
public class ItemImageException extends RuntimeException {

    public ItemImageException (String msg, Throwable cause) {
        super(msg, cause);
    }

}
