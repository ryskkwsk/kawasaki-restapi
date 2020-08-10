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

import java.util.Collections;
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
     *
     * @return  ログの一覧のviewファイル
     */
    @GetMapping("/loglist")
    public String viewAccessLog(SearchAccessLogForm searchAccessLogForm, Model model) {
    // 認証トークンチェック
    if (!tokenSessionInfo.checkToken()) {
        return "redirect:/";
    }
    List<AccessLog> accessLogs = accessLogService.findAll();
    model.addAttribute("logs", accessLogs);
    model.addAttribute("searchAccessLogForm", searchAccessLogForm);
    return "log/loglist";
    }

    /**
     * ログの集計結果を日付検索する
     * @param searchAccessLogForm
     * @param result
     * @param model
     *
     * @return  検索結果をつめたviewファイル
     */
    @GetMapping("/loglist/search")
    public String searchAggregatedLog(@Validated SearchAccessLogForm searchAccessLogForm, BindingResult result, Model model) {

        // 認証トークンチェック
        if (!tokenSessionInfo.checkToken()) {
            return "redirect:/";
        }

        // 開始の日付もしくは終了の日付が空だった場合、エラーメッセージ表示
        if(result.hasErrors()) {
            model.addAttribute("logs","").addAttribute("searchAccessLogForm",searchAccessLogForm);
            return "log/searchLogList";
        } else {
            // 終了の日付が開始の日付より前だった場合、エラーメッセージ表示
            if (searchAccessLogForm.getEndDay().isBefore(searchAccessLogForm.getBeginningDay())) {
                result.reject("error.date.flip",new Object[]{"終了日","開始日"},"");
                model.addAttribute("logs", Collections.emptyList());
                return "log/searchLogList";
            }
            List<SearchAccessLogDto> searchAccessLogDtoList = searchAccessLogService.getSearchAccessLog(searchAccessLogForm.getBeginningDay(), searchAccessLogForm.getEndDay());
            model.addAttribute("logs", searchAccessLogDtoList).addAttribute("searchAccessLogForm",searchAccessLogForm);
        }
        return "log/searchLogList";
    }

}
