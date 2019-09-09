package com.example.kawasakirestapi.domain.service.log;

import com.example.kawasakirestapi.domain.dto.SearchAccessLogDto;
import com.example.kawasakirestapi.domain.repository.log.SearchAccessLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * アクセスログのサービスクラス
 *
 * @author kawasakiryosuke
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchAccessLogService {

    private final SearchAccessLogRepository searchAccessLogRepository;

    /**
     * データベースに保存されているアクセスログの情報を日付指定で取得する
     * @param beginningDay 開始の日付
     * @param endDay   終了の日付
     * @return  一致した日付のアクセスログの情報
     */
    public List<SearchAccessLogDto> getSearchAccessLog(LocalDateTime beginningDay, LocalDateTime endDay) {
        // もし開始の日付と終了の日付が同じだった場合、終了の日付を23:59:59に設定する
        if(beginningDay.compareTo(endDay) == 0) {
            endDay = endDay.plusDays(1).minusSeconds(1);
        }
        return searchAccessLogRepository.findByAggregationDateBetween(beginningDay, endDay);
    }


}
