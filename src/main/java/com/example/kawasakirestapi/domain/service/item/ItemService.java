package com.example.kawasakirestapi.domain.service.item;

import com.example.kawasakirestapi.application.exception.Item.ImageNotFoundException;
import com.example.kawasakirestapi.application.exception.Item.ItemImageException;
import com.example.kawasakirestapi.application.exception.Item.ItemNotFoundException;
import com.example.kawasakirestapi.application.exception.Item.SearchResultNotFoundException;
import com.example.kawasakirestapi.domain.repository.item.ItemRepository;
import com.example.kawasakirestapi.infrastructure.entity.item.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
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
     * 商品全権取得
     *
     * @return 商品情報全権
     */
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    /**
     * 商品1件取得
     * @param id 商品ID
     * @return 商品情報1件
     */
    public Optional<Item> findOneById(Long id) {
        return itemRepository.findById(id);
    }

    /**
     * 保存処理
     *
     * @param item 保存する商品
     * @return Item
     */
    public Item save(Item item){

        Item Item = new Item();
        Item.setTitle(item.getTitle());
        Item.setDescription(item.getDescription());
        Item.setPrice(item.getPrice());

        return itemRepository.save(Item);
    }

    /**
     * 商品更新
     * @param item 編集後の商品情報1件
     * @param id 商品ID
     * @return  編集した商品情報を保存して、データベースに更新した情報を返す
     */
    public Item update(Item item, Long id) {
        Item updateItem = findOneById(id).orElseThrow(() ->new ItemNotFoundException("対象の商品が存在しません"));
        updateItem.setTitle(item.getTitle());
        updateItem.setDescription(item.getDescription());
        updateItem.setPrice(item.getPrice());

        return itemRepository.save(updateItem);
    }

    /**
     * 商品削除
     * @param id 削除する商品のid
     */
    public void deleteById(Long id) {
        Item item = findOneById(id).orElseThrow(() -> new ItemNotFoundException("対象の商品が存在しません"));
        itemRepository.delete(item);
    }


    /**
     * 商品に紐づく画像ファイルを削除
     *
     * @param item 商品情報
     * @return item imagePathを削除した商品情報を返す
     */
    public Item deleteImageItem(Item item) {
        if (StringUtils.isNotEmpty(item.getImagePath())) {
            File file = new File(item.getImagePath());
            if (file.exists()) {
                file.delete();
            }
        }
        if (StringUtils.isNotEmpty(item.getImagePath())){
            item.setImagePath(null);
        }
        return itemRepository.save(item);
    }

    /**
     * 指定(id値によって)の商品に対し画像を投稿。
     * 指定の商品が存在しない場合、例外処理。
     * 画像が正しい形式でない場合、例外処理
     *
     * @param id 商品id         Long
     * @param multipartFile MultipartFile
     * @return item       Item
     */
    public Item uploadImageItem(
            Long id,
            MultipartFile multipartFile) {

//        try (InputStream image = uploadImage.getInputStream()){
//            BufferedImage bufferedImage = ImageIO.read(image);
//            if (bufferedImage == null) {
//                throw new InvalidImageFileException("投稿が画像データではない、もしくは画像が添付されておりません");
//            }
//        } catch (IOException e)   {
//            throw new InvalidImageFileException("投稿が画像データではない、もしくは画像が添付されておりません");
//        }

        // 商品を取得
        Item item = findOneById(id).orElseThrow(() -> new ItemNotFoundException("対象の商品が存在しません"));

        // 画像のファイル名の文字数取得
        int number = multipartFile.getOriginalFilename().lastIndexOf(".");
        // 画像の拡張子取得
        String ext = multipartFile.getOriginalFilename().substring(number);
        // 画像名再設定
        String fileName = id + "_" + getRandomId() + ext;
        // ディレクトリ作成
        mkdirs();
        // アップロードファイルを置く
        File uploadPath = new File(localImagesPath + "/" + fileName).getAbsoluteFile();

        try {
            // 配置したファイルに書き込む
            multipartFile.transferTo(uploadPath);
            String imagePath = localImagesPath + "/" + fileName;
            item.setImagePath(imagePath);

            return itemRepository.save(item);
        } catch (Exception e) {

            throw new ItemImageException("画像のアップロードに失敗しました", e);
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

        if (StringUtils.isEmpty(item.getImagePath())){
            return "";
        }
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
     * 商品検索。
     * 検索結果がない場合は空の配列を返す
     *
     * @param keyword 検索キーワード
     * @return  条件に一致した商品情報をListで返す
     */
    public List<Item> search(String keyword) {

        if (keyword.isEmpty()) {
            throw new SearchResultNotFoundException("ステータスコード200です。検索結果が見つかりませんでした。");
        }
        return itemRepository.findByTitleContaining(keyword);
    }

}