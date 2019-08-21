package com.example.kawasakirestapi.service;

import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.kawasakirestapi.exception.ImageNotFoundException;
import com.example.kawasakirestapi.exception.ImageNotUploadedException;
import com.example.kawasakirestapi.exception.InvalidImageFileException;
import com.example.kawasakirestapi.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ResourceLoader;

import com.example.kawasakirestapi.entity.Item;
import com.example.kawasakirestapi.repository.ItemRepository;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Value("${spring.localImagesPath}")
    private String localImagesPath;

    private final ResourceLoader resourceLoader;

    public ItemService(ItemRepository itemRepository, ResourceLoader resourceLoader) {
        this.itemRepository = itemRepository;
        this.resourceLoader = resourceLoader;
    }

    /**
     * 全ての商品の取得
     * @return 全ての商品をListで返す
     */
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    /**
     * 商品を保存する
     * @param item 保存する商品
     * @return
     */
    public Item save(Item item){

        Item newItem = new Item();
        newItem.setTitle(item.getTitle());
        newItem.setDescription(item.getDescription());
        newItem.setPrice(item.getPrice());

        return itemRepository.save(newItem);
    }

    /**
     * 商品を編集して保存する
     * @param item 編集後の商品
     * @param id 編集する商品のid
     * @return
     */
    public Item update(Item item, long id) {
        Item updateItem = findOneById(id).orElseThrow(() ->new ItemNotFoundException("対象の商品が存在しません"));
        updateItem.setTitle(item.getTitle());
        updateItem.setDescription(item.getDescription());
        updateItem.setPrice(item.getPrice());

        return itemRepository.saveAndFlush(updateItem);
    }

    /**
     * 商品情報を削除する
     * @param id 削除する商品のid
     */
    public void deleteById(Long id) {
        Item item = findOneById(id).orElseThrow(() -> new ItemNotFoundException("対象の商品が存在しません"));
        itemRepository.delete(item);
    }

    /**
     * idから商品情報を取得
     * @param id 取得する商品のid
     * @return 取得した商品情報を返す
     */
    public Optional<Item> findOneById(Long id) {
        return itemRepository.findById(id);
    }

    /**
     * 商品に紐づく画像ファイルを削除
     *
     * @param item Item
     */
    public void deleteImageItem(Item item) {
        File file = new File(item.getImagePath());
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 指定(id値によって)の商品に対し画像を投稿。
     * 指定の商品が存在しない場合、例外処理。
     * 画像が正しい形式でない場合、例外処理
     *
     * @param id          Long
     * @param uploadImage MultipartFile
     * @return item       Item
     */
    public Item uploadImageItem(
            Long id,
            MultipartFile uploadImage) {

        try (InputStream image = uploadImage.getInputStream()){
            BufferedImage bufferedImage = ImageIO.read(image);
            if (bufferedImage == null) {
                throw new InvalidImageFileException("投稿が画像データではない、もしくは画像が添付されておりません");
            }
        } catch (IOException e)   {
            throw new InvalidImageFileException("投稿が画像データではない、もしくは画像が添付されておりません");
        }

        Item item = findOneById(id).orElseThrow(() -> new ItemNotFoundException("対象の商品が存在しません"));
        //画像のファイル名の文字数取得
        int number = uploadImage.getOriginalFilename().lastIndexOf(".");
        //画像の拡張子取得
        String ext = uploadImage.getOriginalFilename().substring(number);
        //画像名再設定
        String fileName = id + "_" + getImageUploadedDate() + ext;
        File uploadPath = new File(localImagesPath + "/" + fileName);

        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(uploadPath))) {
            //アップロードファイルをbyte配列で取得して、ファイルへバイナリデータを書き込む
            bufferedOutputStream.write(uploadImage.getBytes());
            String imagePath = localImagesPath + "/" + fileName;
            item.setImagePath(imagePath);

            return itemRepository.save(item);
        } catch (Exception e) {

            throw new ImageNotUploadedException("画像のアップロードに失敗しました", e);
        }
    }

    /**
     * 現在の日時取得
     *
     * @return 日時 String
     */
    private String getImageUploadedDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS"));
    }

    /**
     * 指定の画像が保存されているディレクトリのパスを返す。
     * 指定の商品が存在しない場合、例外処理
     *
     * @param id Long
     * @return item Item
     */
    private String getLocalImagePath(Long id) {

        Item item = findOneById(id).orElseThrow(() -> new ItemNotFoundException("対象の商品が存在しません"));

        return item.getImagePath();
    }

    /**
     * 指定の画像データを返却。
     * 対象の商品が存在しない場合エラー処理。
     *
     * @param id Long
     * @return 画像データ HttpEntity<byte[]>
     */
    public byte[] getImage(Long id) {

        //画像の格納されているディレクトリを取得
        String imagePath = getLocalImagePath(id);
        //リソースファイルを読み込む
        Resource resource = resourceLoader.getResource("File:" + imagePath);

        byte[] b;
        //byteへ変換
        try (InputStream image = resource.getInputStream()) {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            int c;
            while ((c = image.read()) != -1) {
                bout.write(c);
            }
            b = bout.toByteArray();
        } catch (Exception e) {

            throw new ImageNotFoundException("対象の画像が存在しません", e);
        }

        return b;
    }
    /**
     * 商品の検索。
     * 検索結果がない場合は空の配列を返す
     *
     * @param searchword 検索キーワード
     * @return itemList List<Item> 条件に一致した商品情報をListで返す
     */
    public List<Item> searchItem(String searchword) {

        List<Item> itemList = itemRepository.findByTitleContaining(searchword);
        if (itemList.isEmpty()) {
            return new ArrayList<>();
        }

        return itemList;
    }

}