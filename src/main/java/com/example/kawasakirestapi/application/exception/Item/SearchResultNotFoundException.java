package com.example.kawasakirestapi.application.exception.Item;

import lombok.extern.slf4j.Slf4j;

/**
 *  商品検索情報が見つからなかった時に返す例外クラス
 *
 *  @author kawasakiryosuke
 */
@Slf4j
public class SearchResultNotFoundException extends RuntimeException {

    public SearchResultNotFoundException(String msg) {
        super(msg);
    }

}
