package com.example.kawasakirestapi.domain.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 商品画像以外の商品データを登録するフォーム
 *
 * @author kawasakiryosuke
 */
@Data
public class ItemForm {
    /** 商品タイトル */
    @NotBlank(message = "商品タイトルの入力は必須です。")
    @Size(max = 100, message = "商品タイトルは最大{max}文字までで記入してください")
    private String title;

    /** 価格 */
    @Min(value = 0, message = "金額は0以上の数値を入力してください")
    private Long price;

    /** 説明文 */
    @NotBlank(message = "商品説明文の入力は必須です。")
    @Size(max = 500, message = "商品説明は{max}文字以内で入力してください")
    private String description;
}
