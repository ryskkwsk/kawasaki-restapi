package com.example.kawasakirestapi.exception;

import lombok.extern.slf4j.Slf4j;

/**
 *  対象の投稿が画像ではない、もしくは画像が添付されていないときに返す例外クラス
 */
@Slf4j
public class InvalidImageFileException extends RuntimeException {

    public InvalidImageFileException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidImageFileException(String msg) {
        super(msg);
    }

}
