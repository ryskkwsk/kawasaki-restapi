package com.example.kawasakirestapi.application.controller.item;

import com.example.kawasakirestapi.application.exception.Item.ItemNotFoundException;
import com.example.kawasakirestapi.domain.service.item.ItemService;
import com.example.kawasakirestapi.infrastructure.entity.item.Item;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 商品管理を行うResufulなAPIコントローラー
 *
 * @author kawasakiryosuke
 */
@RestController
public class ItemsController {

    private final ItemService itemService;

    public ItemsController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * 商品全件取得
     *
     * @return 全ての商品を取得し、jsonで送信(0件の場合、空の配列を返す)
     */
    @GetMapping("api/items")
    @ResponseStatus(HttpStatus.OK)
    public List<Item> getItems() {
        return itemService.findAll();
    }

    /**
     * 商品登録
     *
     * @param item 登録する商品
     * @return 登録された商品を取得し、jsonで送信
     */
    @PostMapping("api/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Item create(@RequestBody @Validated Item item) {
        return itemService.save(item);
    }

    /**
     * 商品情報削除
     *
     * @param id 商品ID
     */
    @DeleteMapping("api/items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) {
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
     * 商品更新
     *
     * @param item 更新される商品情報1件
     * @param id 商品ID
     * @return  Item
     */
    @PutMapping("api/items/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Item update(@RequestBody @Validated Item item, @PathVariable("id") long id) {
        return itemService.update(item, id);
    }

    /**
     * 商品画像登録。Multipart形式でない場合、例外を投げる。
     *
     * @param id          画像を登録する商品のid
     * @param multipartFile MultipartFile
     */
    @PostMapping("api/items/image/{id}")
    public Item uploadImageItem(
            @PathVariable("id") long id,
            @RequestParam("image") MultipartFile multipartFile) {

        return itemService.uploadImageItem(id, multipartFile);
    }

    /**
     * 商品画像表示API
     * 画像がアプリ内に存在する場合に結果を返す
     *
     * @param id 画像を表示する商品id
     * @return 画像データ HttpEntity<byte[]>
     */
    @GetMapping ("api/items/image/{id}")
    public HttpEntity<byte[]> showImageItem(@PathVariable Long id) {
        return itemService.getImageItem(id);
    }

    /**
     * 商品検索API
     *
     * @param title  検索するタイトルを含んだ商品情報
     * @return 検索キーワードを含んだ商品を返す
     */
    @GetMapping("api/items/search")
    public List<Item> searchItems(@RequestParam(name = "title", required = false) String title) {
        return itemService.search(title);
    }

}