package com.example.kawasakirestapi.domain.form;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;

/**
 *  アクセスログの集計期間を集計するための期間を受け取るフォーム
 *
 * @author kawasakiryosuke
 */
@Data
public class SearchAccessLogForm {

    @NotEmpty(message = "集計開始日を入力してください")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String beginingDay;

    @NotEmpty(message = "集計終了日をを入力してください")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String endDay;

}
