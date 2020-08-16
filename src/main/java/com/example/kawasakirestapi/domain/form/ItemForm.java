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
    @NotBlank(message = "{error.title.required}")
    @Size(max = 100, message = "{error.title.maxlength}")
    private String title;

    /** 価格 */
    @NotBlank(message = "error.price.required")
    @Min(value = 0, message = "{error.price.zero}")
    private Long price;

    /** 説明文 */
    @NotBlank(message = "{error.description.required}")
    @Size(max = 500, message = "{error.description.maxlength}")
    private String description;
}
