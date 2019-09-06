package com.example.kawasakirestapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * データ交換用クラス
 *
 * @author kawasakiryosuke
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessLogDto {

    private LocalDate aggregationDate;

    private String requestMethod;

    private String requestUrl;

    private Integer statusCode;

    private Integer responseTimes;

    private String uniqueKey;
}
