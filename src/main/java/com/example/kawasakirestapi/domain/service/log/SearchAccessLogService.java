package com.example.kawasakirestapi.domain.service.log;

import com.example.kawasakirestapi.domain.dto.SearchAccessLogDto;
import com.example.kawasakirestapi.domain.repository.log.SearchAccessLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public List<SearchAccessLogDto> getSearchAccessLog(LocalDate beginningDay, LocalDate endDay) {
        return searchAccessLogRepository.findByAggregationDateBetween(beginningDay, endDay);
    }


}
