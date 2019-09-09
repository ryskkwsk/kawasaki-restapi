package com.example.kawasakirestapi.application.controller.log;

import com.example.kawasakirestapi.application.controller.sessioninfo.TokenSessionInfo;
import com.example.kawasakirestapi.domain.dto.SearchAccessLogDto;
import com.example.kawasakirestapi.domain.service.AccessLogService;
import com.example.kawasakirestapi.domain.service.log.SearchAccessLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
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
        if (tokenSessionInfo.checkToken()) {
            model.addAttribute("logs", accessLogService.findAll());
        } else {
            return "redirect:/";
        }
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
        LocalDate beginningDate;
        LocalDate endDate;

        // 認証トークンチェック
        if (!tokenSessionInfo.checkToken()) {
            return "redirect:/";
        }

        if (StringUtils.isEmpty(beginning) || StringUtils.isEmpty(end)) {
            model.addAttribute("errorMessage", "期間を指定してください");
            model.addAttribute("logs","");
        } else {
            beginningDate = accessLogService.convertLocalDate(beginning, "yyyy-MM-dd");
            endDate = accessLogService.convertLocalDate(end, "yyyy-MM-dd");

            if (endDate.isBefore(beginningDate)) {
                model.addAttribute("errorMessage", "入力された期間が不正です。");
            }
            List<SearchAccessLogDto> list = searchAccessLogService.getSearchAccessLog(beginningDate, endDate);
            model.addAttribute("logs", list);

        }
        return "log/searchLogList";
    }

}
