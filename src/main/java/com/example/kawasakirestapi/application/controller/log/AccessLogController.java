package com.example.kawasakirestapi.application.controller.log;

import com.example.kawasakirestapi.application.controller.sessioninfo.TokenSessionInfo;
import com.example.kawasakirestapi.domain.dto.SearchAccessLogDto;
import com.example.kawasakirestapi.domain.service.log.AccessLogService;
import com.example.kawasakirestapi.domain.service.log.SearchAccessLogService;
import com.example.kawasakirestapi.infrastructure.entity.log.AccessLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * アクセスログを扱うコントローラー
 *
 * @author kawasakiryosuke
 */
@Controller
@RequiredArgsConstructor
public class AccessLogController {
    private final AccessLogService accessLogService;

    private final SearchAccessLogService searchAccessLogService;

    private final TokenSessionInfo tokenSessionInfo;

    /**
     * 認証トークンがあれば、アクセスログを取得してログの一覧のviewを返す。無ければトップページに遷移。
     * @param model
     * @return  ログの一覧のviewファイル
     */
    @GetMapping("/loglist")
    public String viewAccessLog(Model model) {
    // 認証トークンチェック
    if (!tokenSessionInfo.checkToken()) {
        return "redirect:/";
    }
    List<AccessLog> accessLogs = accessLogService.findAll();
    model.addAttribute("logs", accessLogs);
    return "log/loglist";
    }

    /**
     *  ログの集計結果を日付検索する
     * @param beginning
     * @param end
     * @param model
     * @return  検索結果をつめたviewファイル
     */
    @GetMapping("/loglist/search")
    public String searchAggregatedLog(@RequestParam("from") String beginning, @RequestParam("until") String end, Model model) {

        // 認証トークンチェック
        if (!tokenSessionInfo.checkToken()) {
            return "redirect:/";
        }

        if (StringUtils.isEmpty(beginning) || StringUtils.isEmpty(end)) {
            model.addAttribute("errorMessage", "期間を指定してください");
            model.addAttribute("logs","");
        } else {
            LocalDateTime beginningDay = accessLogService.convertLocalDateTime(beginning);
            LocalDateTime endDay= accessLogService.convertLocalDateTime(end);

            if (endDay.isBefore(beginningDay)) {
                model.addAttribute("errorMessage", "入力された期間が不正です。");
            }
            List<SearchAccessLogDto> searchAccessLogDtoList = searchAccessLogService.getSearchAccessLog(beginningDay, endDay);
            model.addAttribute("logs", searchAccessLogDtoList);

        }
        return "log/searchLogList";
    }

}
