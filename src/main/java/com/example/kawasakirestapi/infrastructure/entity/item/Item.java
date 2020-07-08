package com.example.kawasakirestapi.infrastructure.entity.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * itemsテーブルのエンティティ
 *
 * @author kawasakiryosuke
 */
@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Serializable{

    /** ID **/
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    /** 商品タイトル **/
    @Column(nullable = false)
    @NotEmpty(message = "商品のタイトルを入力してください")
    @Length(max = 100, message = "商品タイトルは{max}文字以内で入力してください")
    private String title;

    /** 価格 **/
    @Column(nullable = false)
    @NotNull(message = "必須項目です")
    @Min(value = 0, message = "金額は0以上の数値を入力してください")
    private Long price;

    /** 説明文 **/
    @Length(max = 500, message = "商品説明は{max}文字以内で入力してください")
    @Column(nullable = false)
    private String description;

    /** 画像へのパス **/
    @Column
    private String imagePath;

}
