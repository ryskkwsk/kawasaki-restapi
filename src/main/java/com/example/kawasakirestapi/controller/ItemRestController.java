package com.example.kawasakirestapi.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
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
public class ItemRestController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    public ItemRestController(ItemService itemService, ItemRepository itemRepository) {
        this.itemService = itemService;
        this.itemRepository = itemRepository;
    }

    /**
     * 商品取得用コントローラー
     * @return 全ての商品を取得し、jsonで送信(0件の場合、空の配列を返す)
     */
    @GetMapping("v1/items")
    @ResponseBody
    public List<Item> getItems() {
        List<Item> items = itemService.findAll();
        return items;
    }

    /**
     * 商品登録用コントローラー
     * @param item 登録する商品
     * @return 登録された商品を取得し、jsonで送信
     */
    @PostMapping("v1/items")
    public Item createItem(@RequestBody @Validated Item item) {
        return itemService.save(item);
    }

    /**
     * 商品情報削除用コントローラー
     * @param id 削除する商品のid
     */
    @DeleteMapping("v1/items/{id}")
    public void deleteItem(@PathVariable("id") long id) {
        itemService.deleteById(id);
    }

    /**
     * 商品画像削除用コントローラー
     * @param id 削除する商品のid
     */
    @DeleteMapping("v1/items/image/{id}")
    public void deleteItemImage(@PathVariable("id") long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("対象の商品が存在しません"));
        itemService.deleteImageItem(item);
    }

    /**
     * 商品編集用コントローラー
     * @param item 編集する商品
     * @param id 編集する商品のid
     * @return 編集された商品をjsonで送信
     */
    @PutMapping("v1/items/{id}")
    public Item editItem(@RequestBody @Validated Item item, @PathVariable("id") long id) {
        return itemService.update(item, id);
    }

    /**
     * 画像の登録機能。Multipart形式でない場合、例外を投げる。
     *
     * @param id          画像を登録する商品のid
     * @param uploadImage MultipartFile
     */
    @PostMapping("v1/items/image/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Item uploadImageItem(
            @PathVariable("id") long id,
            @RequestParam("image") MultipartFile uploadImage) {

        //画像が添付されているかチェック、添付されている場合、画像かどうかの判定
        try (InputStream image = uploadImage.getInputStream()) {
            BufferedImage bufferedImage = ImageIO.read(image);
            //投稿データがnullの場合、画像がでない
            if (bufferedImage != null) {
                Item postImageItem = itemService.uploadImageItem(id, uploadImage);
                return postImageItem;
            } else {
                throw new InvalidImageFileException("不適切な画像データです");
            }
        } catch (IOException e) {

            throw new InvalidImageFileException("不適切な画像データです", e);
        }
    }

    /**
     * 画像を表示する機能。
     * 画像がアプリ内に存在する場合に結果を返す
     *
     * @param id 画像を表示する商品id
     * @return 画像データ HttpEntity<byte[]>
     */
    @ResponseBody
    @GetMapping ("v1/items/image/{id}")
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
     * 引数のsearchwordから商品タイトルを検索。
     *
     * @param searchword string 検索キーワード
     * @return 検索キーワードを含んだ商品を返す
     */
    @ResponseBody
    @GetMapping("v1/items/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Item> searchItems(@RequestParam("q") String searchword) {

        if (StringUtils.isEmptyOrWhitespace(searchword)) {
            return new ArrayList<>();
        }
        List<Item> items = itemService.searchItem(searchword);

        return items;
    }

}