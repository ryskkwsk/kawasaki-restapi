package com.example.kawasakirestapi.domain.form;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 *  アクセスログの集計期間を集計するための期間を受け取るフォーム
 *
 * @author kawasakiryosuke
 */
@Data
public class SearchAccessLogForm {

    @NotNull(message = "集計開始日を入力してください")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate beginningDay;

    @NotNull(message = "集計終了日を入力してください")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDay;

}
