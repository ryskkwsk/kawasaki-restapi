package com.example.kawasakirestapi.exception;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 例外のハンドラクラス。
 *
 */
@RestControllerAdvice
public class ItemExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ItemExceptionHandler.class);

    /**
     * 500エラー発生時にエラーを返す
     *
     * @param ex      Exception
     * @param request リクエスト内容
     * @return 500エラー情報を返す
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternalServerErrorException(Exception ex, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse body = new ErrorResponse(status, "内部エラーが発生しております。");

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * 商品情報が存在しない時にエラーを返す
     * @param ex    Exception
     * @param request リクエスト内容
     * @return 404エラー情報を返す
     */
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException ex, WebRequest request) {
        logger.warn(ex.getMessage(), ex);
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse body = new ErrorResponse(status, ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * 画像がデータベースに格納されているパスに存在しない時にエラーを返す
     * @param ex    Exception
     * @param request リクエスト内容
     * @return 404エラー情報を返す
     */
    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<Object> handleImageNotFoundException(ImageNotFoundException ex, WebRequest request) {
        logger.warn(ex.getMessage(), ex);
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse body = new ErrorResponse(status, ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * 画像が正しくアップロードされなかった時にエラーを返す
     * @param ex    Exception
     * @param request   リクエスト内容
     * @return 500エラー情報を返す
     */
    @ExceptionHandler(ImageNotUploadedException.class)
    public ResponseEntity<Object> handleImageNotUploadedException(ImageNotUploadedException ex, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse body = new ErrorResponse(status, ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * MultipartFormからファイルが送られてこなかった時にエラーを返す
     * @param ex    Exception
     * @param request   リクエスト内容
     * @return 400エラー情報を返す
     */
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Object> handleFileUploadErrorException(MultipartException ex, WebRequest request){
        String errorMessage = "画像ファイルではありません";
        logger.error(ex.getMessage(), ex);
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse body = new ErrorResponse(status, errorMessage);

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * 投稿データが画像データでない場合にエラーを返す
     *
     * @param ex      例外
     * @param request リクエスト
     * @return 400エラー情報を返す
     */
    @ExceptionHandler({ InvalidImageFileException.class })
    public ResponseEntity<Object> invalidImageException(InvalidImageFileException ex, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse body = new ErrorResponse(status, ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, request);
    }


}