package com.example.kawasakirestapi.infrastructure.entity.log;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.*;

/**
 * アクセスログのエンティティ
 *
 * @author kawasakiryosuke
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "access_log")
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "aggregation_date")
    private LocalDate aggregationDate;

    @Column(name = "request_method")
    private String requestMethod;

    @Column(name = "request_url")
    private String requestUrl;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "access_count")
    private Integer accessCount;

    @Column(name = "response_times")
    private Integer responseTimes;

    /**
     * アクセスカウントを１足す
     */
    public void countUp() {
        this.setAccessCount(this.getAccessCount() + 1);
    }

    /**
     * レスポンスタイムの平均をだす
     * @param responseTimes
     */
    public void calculateAverage(Integer responseTimes) {
        this.setResponseTimes((this.getResponseTimes() * (this.getAccessCount() - 1) + responseTimes) / this.getAccessCount());
    }

}