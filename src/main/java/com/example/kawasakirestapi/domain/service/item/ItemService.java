package com.example.kawasakirestapi.domain.service.item;

import com.example.kawasakirestapi.application.exception.Item.*;
import com.example.kawasakirestapi.domain.repository.ItemRepository;
import com.example.kawasakirestapi.infrastructure.entity.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 商品管理を行うサービス
 *
 * @author kawasakiryosuke
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;

    @Value("${spring.localImagesPath}")
    private String localImagesPath;

    private final ResourceLoader resourceLoader;

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
     * @return データベースに保存した商品情報を返す
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
     * @return  編集した商品情報を保存して、データベースに更新した情報を返す
     */
    public Item update(Item item, long id) {
        Item updateItem = findOneById(id).orElseThrow(() ->new ItemNotFoundException("対象の商品が存在しません"));
        updateItem.setTitle(item.getTitle());
        updateItem.setDescription(item.getDescription());
        updateItem.setPrice(item.getPrice());

        return itemRepository.save(updateItem);
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
     * @param item 商品情報
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
     * @param id 商品id         Long
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
        // 画像のファイル名の文字数取得
        int number = uploadImage.getOriginalFilename().lastIndexOf(".");
        // 画像の拡張子取得
        String ext = uploadImage.getOriginalFilename().substring(number);
        // 画像名再設定
        String fileName = id + "_" + getRandomId() + ext;
        // ディレクトリ作成
        mkdirs();
        // アップロードファイルを置く
        File uploadPath = new File(localImagesPath + "/" + fileName).getAbsoluteFile();

        try {
            // 配置したファイルに書き込む
            uploadImage.transferTo(uploadPath);
            String imagePath = localImagesPath + "/" + fileName;
            item.setImagePath(imagePath);

            return itemRepository.save(item);
        } catch (Exception e) {

            throw new ImageNotUploadedException("画像のアップロードに失敗しました", e);
        }
    }

    /**
     * 画像配置するディレクトリの作成
     */
    private void mkdirs() {
        try {
            File dir = new File(localImagesPath);
            dir.mkdir();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 重複のない一意なユニークなIDを取得
     *
     * @return 生成されたユニークな値
     */
      private String getRandomId() {
          UUID uuid = UUID.randomUUID();
          return  uuid.toString();
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
     * @param id 商品id
     * @return 画像データ HttpEntity<byte[]>
     */
    public HttpEntity<byte[]> getImageItem(Long id) {

        // 画像の格納されているディレクトリを取得
        String imagePath = getLocalImagePath(id);
        // リソースファイルを読み込む
        Resource resource = resourceLoader.getResource("File:" + imagePath);

        byte[] imageBytes;
        // byteへ変換
        try (InputStream is = resource.getInputStream()) {
            imageBytes = IOUtils.toByteArray(is);
        } catch (Exception e) {

            throw new ImageNotFoundException("対象の画像が存在しません", e);
        }
        byte[] images = imageBytes;

        HttpHeaders headers = new HttpHeaders();
        try (InputStream inputStream = new ByteArrayInputStream(images)) {
            String contentType = URLConnection.guessContentTypeFromStream(inputStream);
            headers.setContentType(MediaType.valueOf(contentType));
            headers.setContentLength(images.length);
        } catch (Exception e) {
            throw new ImageNotFoundException("対象の画像が存在しません", e);
        }

        return new ResponseEntity<>(images,headers, HttpStatus.OK);
    }


    /**
     * 商品の検索。
     * 検索結果がない場合は空の配列を返す
     *
     * @param searchword 検索キーワード
     * @return itemList List<Item> 条件に一致した商品情報をListで返す
     */
    public List<Item> searchItem(String searchword) {

        if (searchword.isEmpty()) {
            throw new SearchResultNotFoundException("ステータスコード200です。検索結果が見つかりませんでした。");
        }
        return itemRepository.findByTitleContaining(searchword);
    }

}