package com.example.kawasakirestapi.domain.repository.log;

import com.example.kawasakirestapi.domain.dto.SearchAccessLogDto;
import com.example.kawasakirestapi.infrastructure.entity.log.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * ログファイルを集計するリポジトリ
 *
 * @author kawasakiryosuke
 */
public interface SearchAccessLogRepository extends JpaRepository<AccessLog, Long> {

    /**
     * 開始の日付と終わりの日付の間の一定範囲内の値を検索
     * @param beginningDay 開始の日付
     * @param endDay   終わりの日付
     * @return  一定範囲内に合致した日付の情報を返す
     */
    @Query(value = "SELECT request_url AS requestUrl, aggregation_date AS aggregationDate, status_code AS statusCode, request_method AS requestMethod, SUM(access_count) AS totalAccessCount, AVG(response_times) AS averageResponseTime FROM access_log WHERE aggregation_date BETWEEN :beginningDay AND :endDay GROUP BY request_url, status_code", nativeQuery = true)
    List<SearchAccessLogDto> findByAggregationDateBetween(@Param("beginningDay") LocalDate beginningDay, @Param("endDay")LocalDate endDay);

}
