package com.example.kawasakirestapi.exception;

import lombok.extern.slf4j.Slf4j;

/**
 *  画像のアップロードに失敗した時に返す例外クラス
 */
@Slf4j
public class ImageNotUploadedException extends RuntimeException {

    public ImageNotUploadedException (String msg, Throwable cause) {
        super(msg, cause);
    }

}
