package com.example.kawasakirestapi.domain.form;


import com.example.kawasakirestapi.domain.form.validationorder.First;
import com.example.kawasakirestapi.domain.form.validationorder.Second;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 *  アクセスログの集計期間を集計するための期間を受け取るフォーム
 *
 * @author kawasakiryosuke
 */
@Data
public class SearchAccessLogForm {

    @NotEmpty(message = "集計開始日を入力してください", groups= First.class)
    @Pattern(regexp = "^\\d{4}-\\d{1,2}-\\d{1,2}$", message = "入力された値は正しくありません。", groups = Second.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String beginingDay;

    @NotEmpty(message = "集計終了日をを入力してください", groups = First.class)
    @Pattern(regexp = "^\\d{4}-\\d{1,2}-\\d{1,2}$", message = "入力された値は正しくありません。", groups = Second.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String endDay;

}
