package com.example.kawasakirestapi.application.controller.log;

import com.example.kawasakirestapi.application.controller.sessioninfo.TokenSessionInfo;
import com.example.kawasakirestapi.domain.dto.SearchAccessLogDto;
import com.example.kawasakirestapi.domain.form.SearchAccessLogForm;
import com.example.kawasakirestapi.domain.service.log.AccessLogService;
import com.example.kawasakirestapi.domain.service.log.SearchAccessLogService;
import com.example.kawasakirestapi.infrastructure.entity.log.AccessLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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
    if (!tokenSessionInfo.checkToken()) {
        return "redirect:/";
    }
    List<AccessLog> accessLogs = accessLogService.findAll();
    model.addAttribute("logs", accessLogs);
    model.addAttribute("searchAccessLogForm", new SearchAccessLogForm());
    return "log/loglist";
    }

    /**
     * ログの集計結果を日付検索する
     * @param searchAccessLogForm
     * @param result
     * @param model
     * @return  検索結果をつめたviewファイル
     */
    @GetMapping("/loglist/search")
    public String searchAggregatedLog(@ModelAttribute @Validated SearchAccessLogForm searchAccessLogForm, BindingResult result, Model model) {

        // 認証トークンチェック
        if (!tokenSessionInfo.checkToken()) {
            return "redirect:/";
        }

        // 開始の日付もしくは終了の日付が空だった場合、エラーメッセージ表示
        if(result.hasErrors()) {
            model.addAttribute("errorMessage", "期間を指定してください");
            model.addAttribute("logs","");
        } else {
            // 開始の日付、終了の日付をLocalDateTime型に変換
            LocalDate beginningDay = accessLogService.convertLocalDate(searchAccessLogForm.getBeginingDay(),"yyyy-MM-dd");
            LocalDate endDay= accessLogService.convertLocalDate(searchAccessLogForm.getEndDay(),"yyyy-MM-dd");

            // 終了の日付が開始の日付より前だった場合、エラーメッセージ表示
            if (endDay.isBefore(beginningDay)) {
                model.addAttribute("errorMessage", "入力された期間が不正です。");
            }
            List<SearchAccessLogDto> searchAccessLogDtoList = searchAccessLogService.getSearchAccessLog(beginningDay, endDay);
            model.addAttribute("logs", searchAccessLogDtoList).addAttribute("searchAccessLogForm",searchAccessLogForm);
        }
        return "log/searchLogList";
    }

}
