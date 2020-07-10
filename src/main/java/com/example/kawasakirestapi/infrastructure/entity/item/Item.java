package com.example.kawasakirestapi.infrastructure.entity.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private String title;

    /** 価格 **/
    @Column(nullable = false)
    private Long price;

    /** 説明文 **/
    @Column(nullable = false)
    private String description;

    /** 画像へのパス **/
    @Column
    private String imagePath;

}
