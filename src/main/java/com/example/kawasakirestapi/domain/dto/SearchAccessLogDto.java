package com.example.kawasakirestapi.domain.dto;

import java.time.LocalDate;

/**
 *  アクセスログ検索結果受け取る
 *
 * @author kawasakiryosuke
 */
public interface SearchAccessLogDto {

    LocalDate getAggregationDate();

    String getRequestMethod();

    String getRequestUrl();

    Integer getStatusCode();

    Integer getTotalAccessCount();

    Integer getAverageResponseTime();
}