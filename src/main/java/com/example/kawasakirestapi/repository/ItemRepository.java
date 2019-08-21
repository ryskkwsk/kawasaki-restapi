package com.example.kawasakirestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kawasakirestapi.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{

    /**
     * 一致した商品idの商品を返す
     * @param id 商品id
     * @return
     */
    Optional<Item> findById(Long id);

    /**
     * searchwordと部分一致するtitleをもつ商品を検索
     * @param searchword 検索用キーワード
     * @return 該当した商品をListで返す
     */
    List<Item> findByTitleContaining(String searchword);
}