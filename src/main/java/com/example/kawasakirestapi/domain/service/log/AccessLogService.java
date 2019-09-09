package com.example.kawasakirestapi.domain.service.log;

import com.example.kawasakirestapi.domain.dto.AccessLogDto;
import com.example.kawasakirestapi.domain.repository.log.AccessLogRepository;
import com.example.kawasakirestapi.domain.repository.log.SearchAccessLogRepository;
import com.example.kawasakirestapi.domain.setting.AccessLogSetting;
import com.example.kawasakirestapi.infrastructure.entity.log.AccessLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * アクセスログのサービスクラス
 *
 * @author kawasakiryosuke
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccessLogService {

    private final AccessLogRepository accessLogRepository;

    private final SearchAccessLogRepository searchAccessLogRepository;

    private final AccessLogSetting accessLogSetting;

    /**
     * データベースに保存されているアクセスログ情報を取得する
     * @return  全てのアクセスログ情報
     */
    public List<AccessLog> findAll() {
        return accessLogRepository.findAll();
    }

    /**
     * 昨日の日付のアクセスログを集計してデータベースに保存する
     * @throws IOException
     */
    public void aggregateAccessLog() throws IOException {

        Path filePath = createYesterdayFilePath();
        if(!Files.exists(filePath)){
            log.info("本日のログファイルはありませんでした");
            return;
        }

        if(isAlreadyAggregated()){
            log.info("本日のログは集計済みです");
            return;
        }

        Map<String, AccessLog> map = new HashMap<>();
        try (Stream<String> logStream = Files.lines(filePath)) {
            log.info("{}を読み込みます",filePath);
            logStream.map(this::splitLine)
                     .forEach(accessLogDto -> {
                         String key = accessLogDto.getUniqueKey();
                         if (map.containsKey(key)) {
                             setAggregateSameLog(key, accessLogDto, map);
                         } else {
                             setNewAccessLog(key, accessLogDto, map);
                         }
                     });
        }
        accessLogRepository.saveAll(map.values());
    }

    /**
     * 本日の日付のアクセスログがデータベースに保存されていた場合、tureを返す
     * @return true or false
     */
    private boolean isAlreadyAggregated() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<AccessLog> accessLogsOfYesterday = accessLogRepository.findByAggregationDate(yesterday);
        return !accessLogsOfYesterday.isEmpty();
    }

    /**
     *  ログファイルの行をスペースで区切ってリストに格納。それぞれ変数に格納してAccessLogDtoに渡す。
     * @param logLine
     * @return  AccessLogDto
     */
    private AccessLogDto splitLine(String logLine) {
        List<String> columns = Arrays.asList(logLine.split(" ",-1));
        String yearMonthDate = columns.get(0);
        String hourMinutesSecond = columns.get(1);
        LocalDate accessDate = convertLocalDate(yearMonthDate +  " " + hourMinutesSecond,"yyyy/MM/dd HH:mm:ss");
        String requestMethod = columns.get(2);
        String requestUrl = columns.get(3);
        String statusCode = columns.get(4);
        String responseTime = columns.get(5);

        return saveAccessLogDto(requestMethod, requestUrl, statusCode, responseTime, accessDate);
    }

    /**
     * accessLogDtoにアクセスログ情報を保存
     * @param requestMethod
     * @param requestUrl
     * @param statusCode
     * @param responseTime
     * @param accessDate
     * @return  保存されたaccessLogDtoを返す
     */
    private AccessLogDto saveAccessLogDto(String requestMethod, String requestUrl, String statusCode, String responseTime, LocalDate accessDate) {
        AccessLogDto accessLogDto = new AccessLogDto();
        // キー作成
        String uniqueKey = requestMethod + requestUrl + statusCode;
        // 集計日付をaccessLogDtoに保存
        accessLogDto.setAggregationDate(accessDate);
        // リクエストメソッドをaccessLogDtoに保存
        accessLogDto.setRequestMethod(requestMethod);
        // リクエストURLをaccessLogDtoに保存
        accessLogDto.setRequestUrl(requestUrl);
        // ステータスコードをaccessLogDtoに保存
        accessLogDto.setStatusCode(Integer.parseInt(statusCode));
        // リクエスト処理にかかった時間をaccessLogDtoに保存
        accessLogDto.setResponseTimes(Integer.parseInt(responseTime));
        // キーをaccessLogDtoに保存
        accessLogDto.setUniqueKey(uniqueKey);
        return accessLogDto;
    }

    /**
     * アクセスログをAccessLogエンティティに保存
     * @param key
     * @param accessLogDto
     * @param map
     */
    private void setNewAccessLog(String key, AccessLogDto accessLogDto, Map<String, AccessLog> map) {
        AccessLog accessLog = new AccessLog();
        accessLog.setAggregationDate(accessLogDto.getAggregationDate());
        accessLog.setRequestMethod(accessLogDto.getRequestMethod());
        accessLog.setRequestUrl(accessLogDto.getRequestUrl());
        accessLog.setStatusCode(accessLogDto.getStatusCode());
        accessLog.setAccessCount(1);
        accessLog.setResponseTimes(accessLogDto.getResponseTimes());
        map.put(key, accessLog);
    }

    /**
     * uniqukeyが同じだった場合、アクセスカウントとレスポンスタイムの処理をする
     * @param key
     * @param accessLogDto
     * @param map
     */
    private void setAggregateSameLog(String key, AccessLogDto accessLogDto, Map<String, AccessLog> map) {
        AccessLog accessLog = map.get(key);
        accessLog.countUp();
        accessLog.calculateAverage(accessLogDto.getResponseTimes());
    }

    /**
     * 日付をStringからLocalDateに変換
     * @param date
     * @param format
     * @return  変換した日付を返す
     */
    public LocalDate convertLocalDate(String date, String format) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
    }

    /**
     * 日付をStringからLocalDateTimeに変換
     * @param date
     * @return  変換したLocalDateTime型の日付を返す
     */
    public LocalDateTime convertLocalDateTime(String date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(accessLogSetting.getDatetimeFormat());
        String replaceDate = date.replaceAll("-", "/");
        LocalDate localDate = LocalDate.parse(replaceDate, dtf);
        return LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth(), 0, 0, 0);
    }

    /**
     * 昨日の日付のログファイルパスを生成
     * @return  生成したログファイルパスを返す
     */
    private Path createYesterdayFilePath() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(yesterday);

        return Paths.get(accessLogSetting.getAccessLogPath() + date + ".log").toAbsolutePath();
    }


}
