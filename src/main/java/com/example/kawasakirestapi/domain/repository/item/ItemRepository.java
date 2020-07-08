package com.example.kawasakirestapi.domain.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kawasakirestapi.infrastructure.entity.item.Item;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Itemのリポジトリ
 *
 * @author kawasakiryosuke
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{

    /**
     * 一致した商品idの商品を返す
     * @param id 商品id
     * @return 商品idに該当した商品をOptionalで返す
     */
    Optional<Item> findById(Long id);

    /**
     * keywordと部分一致するtitleをもつ商品を検索
     * @param keyword 検索用キーワード
     * @return 該当した商品をListで返す
     */
    List<Item> findByTitleContaining(String keyword);
}