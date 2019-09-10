package com.example.kawasakirestapi.domain.batch;

import com.example.kawasakirestapi.domain.service.log.AccessLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * バッチ処理の起点のクラス
 * 毎日10時にバッチ処理が開始される
 *
 * @author kawasakiryosuke
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AccessLogBatch {
    private final AccessLogService accessLogService;

    /**
     *  毎朝10時にアクセスログの集計を開始する
     */
    @Scheduled(cron = "0 0 10 * * *", zone = "Asia/Tokyo")
    public void doAggregation() {
    log.info("-------------------------------------------------");
    log.info("アクセスログの集計開始 : start");
        try {
            accessLogService.aggregateAccessLog();
            log.info("-------------------------------------------------");
            log.info("アクセスログの集計終了 : successful");
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            log.warn("アクセスログ集計中に問題が発生しました");
            log.warn("アクセスログを終了します : failed");
            log.info("-------------------------------------------------");
        }
    }
}
