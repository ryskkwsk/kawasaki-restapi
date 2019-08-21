package com.example.kawasakirestapi.entity;

import java.io.File;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Serializable{

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "商品のタイトルを入力してください")
    @Length(max = 100, message = "商品タイトルは{max}文字以内で入力してください")
    private String title;

    @Column(nullable = false)
    @NotNull(message = "必須項目です")
    @Min(value = 0, message = "0以上の数値をご入力ください")
    private Long price;

    @Length(max = 500, message = "商品説明は{max}文字以内で入力してください")
    @Column
    private String description;

    @Column
    private String image;

}
