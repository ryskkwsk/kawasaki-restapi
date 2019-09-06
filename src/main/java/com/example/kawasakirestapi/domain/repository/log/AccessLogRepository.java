package com.example.kawasakirestapi.domain.repository.log;

import com.example.kawasakirestapi.infrastructure.entity.log.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * アクセスログのリポジトリ
 *
 * @author kawasakiryosuke
 */
@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
     /**
     * 引数の日付と一致した日付のアクセスログ情報を取得
     * @param yesterday
     * @return  一致した日付の情報を返す
     */
    List<AccessLog> findByAggregationDate(LocalDate yesterday);
}
