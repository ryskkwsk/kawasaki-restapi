package com.example.kawasakirestapi.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.kawasakirestapi.exception.ImageNotFoundException;
import com.example.kawasakirestapi.exception.InvalidImageFileException;
import com.example.kawasakirestapi.exception.ItemNotFoundException;
import com.example.kawasakirestapi.repository.ItemRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.kawasakirestapi.entity.Item;
import com.example.kawasakirestapi.service.ItemService;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.imageio.ImageIO;

@RestController
public class ItemsController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    public ItemsController(ItemService itemService, ItemRepository itemRepository) {
        this.itemService = itemService;
        this.itemRepository = itemRepository;
    }

    /**
     * 商品取得API
     * @return 全ての商品を取得し、jsonで送信(0件の場合、空の配列を返す)
     */
    @GetMapping("api/items")
    public List<Item> getItems() {
        return itemService.findAll();
    }

    /**
     * 商品登録API
     * @param item 登録する商品
     * @return 登録された商品を取得し、jsonで送信
     */
    @PostMapping("api/items")
    public Item createItem(@RequestBody @Validated Item item) {
        return itemService.save(item);
    }

    /**
     * 商品情報削除API
     * @param id 削除する商品のid
     */
    @DeleteMapping("api/items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable("id") long id) {
        itemService.deleteById(id);
    }

    /**
     * 商品画像削除API
     * @param id 削除する商品のid
     */
    @DeleteMapping("api/items/image/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItemImage(@PathVariable("id") long id) {
        Item item = itemService.findOneById(id).orElseThrow(() -> new ItemNotFoundException("対象の商品が存在しません"));
        itemService.deleteImageItem(item);
    }

    /**
     * 商品編集API
     * @param item 編集する商品
     * @param id 編集する商品のid
     * @return 編集された商品をjsonで送信
     */
    @PutMapping("api/items/{id}")
    public Item editItem(@RequestBody @Validated Item item, @PathVariable("id") long id) {
        return itemService.update(item, id);
    }

    /**
     * 商品画像登録API。Multipart形式でない場合、例外を投げる。
     *
     * @param id          画像を登録する商品のid
     * @param uploadImage MultipartFile
     */
    @PostMapping("api/items/image/{id}")
    public Item uploadImageItem(
            @PathVariable("id") long id,
            @RequestParam("image") MultipartFile uploadImage)  {

        return itemService.uploadImageItem(id, uploadImage);
    }

    /**
     * 商品画像表示API
     * 画像がアプリ内に存在する場合に結果を返す
     *
     * @param id 画像を表示する商品id
     * @return 画像データ HttpEntity<byte[]>
     */
    @GetMapping ("api/items/image/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity<byte[]> showImageItem(@PathVariable Long id) {

        byte[] image = itemService.getImage(id);
        //レスポンスデータとして返却
        HttpHeaders headers = new HttpHeaders();
        try (InputStream inputStream = new ByteArrayInputStream(image)) {
            String contentType = URLConnection.guessContentTypeFromStream(inputStream);
            headers.setContentType(MediaType.valueOf(contentType));
            headers.setContentLength(image.length);
        } catch (Exception e) {
            throw new ImageNotFoundException("対象の画像が存在しません", e);
        }

        return new HttpEntity<>(image, headers);
    }

    /**
     * 商品検索API
     *
     * @param searchword string 検索キーワード
     * @return 検索キーワードを含んだ商品を返す
     */
    @GetMapping("api/items/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Item> searchItems(@RequestParam("q") String searchword) {

        if (StringUtils.isEmptyOrWhitespace(searchword)) {
            return new ArrayList<>();
        }

        return itemService.searchItem(searchword);
    }

}