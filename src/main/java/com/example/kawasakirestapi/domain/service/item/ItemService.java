package com.example.kawasakirestapi.domain.service.item;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.kawasakirestapi.application.exception.Item.ItemNotFoundException;
import com.example.kawasakirestapi.application.exception.Item.SearchResultNotFoundException;
import com.example.kawasakirestapi.domain.form.ItemForm;
import com.example.kawasakirestapi.domain.repository.item.ItemRepository;
import com.example.kawasakirestapi.domain.service.aws.AwsS3Service;
import com.example.kawasakirestapi.domain.setting.AwsS3Setting;
import com.example.kawasakirestapi.domain.setting.ImageSetting;
import com.example.kawasakirestapi.infrastructure.entity.item.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

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

    private final AwsS3Service awsS3Service;

    private final ImageSetting imageSetting;

    private final AwsS3Setting awsS3Setting;

    private final ItemImageService itemImageService;


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
    public Item findById(Long id) {
        return itemRepository.findById(id).orElseThrow(() ->new ItemNotFoundException("対象の商品が存在しません"));
    }

    /**
     * 保存処理
     *
     * @param itemForm 商品画像以外の商品データを受け付けるフォーム
     * @return Item
     */
    public Item save(ItemForm itemForm){

        Item Item = new Item();
        Item.setTitle(itemForm.getTitle());
        Item.setDescription(itemForm.getDescription());
        Item.setPrice(itemForm.getPrice());

        return itemRepository.save(Item);
    }

    /**
     * 商品更新
     * @param itemForm 商品画像以外の商品データを受け付けるフォーム
     * @param id 商品ID
     * @return  編集した商品情報を保存して、データベースに更新した情報を返す
     */
    public Item update(ItemForm itemForm, Long id) {
        Item item = findById(id);
        item.setTitle(itemForm.getTitle());
        item.setDescription(itemForm.getDescription());
        item.setPrice(itemForm.getPrice());

        return itemRepository.save(item);
    }

    /**
     * 商品削除
     * @param id 削除する商品のid
     */
    public void deleteById(Long id) {
        Item item = findById(id);
        awsS3Service.deleteS3Object(awsS3Setting.getImageDir() + item.getImagePath());
        itemRepository.delete(item);
    }


    /**
     * 商品に紐づく画像ファイルを削除
     *
     * @param item 商品情報
     * @return item imagePathを削除した商品情報を返す
     */
    public void deleteImageItem(Item item) {
        if (StringUtils.isNotEmpty(item.getImagePath())) {
            // s3の画像削除
            awsS3Service.deleteS3Object(awsS3Setting.getImageDir() + item.getImagePath());
            item.setImagePath(null);
        }
        itemRepository.save(item);
    }

    /**
     * 商品画像と画像へのパスを削除する
     *
     * @param id 商品画像と画像へのパスを削除する商品のID
     */
//    public void deleteImagePath(Long id) {
//        Item item = findById(id);
//        if (item.getImagePath() != null) {
//            itemImageService.deleteFile(item.getImagePath());
//            item.setImagePath(null);
//            itemRepository.save(item);
//        }
//    }

    /**
     * 商品画像の保存先へのパスを登録する
     *
     * @param id 該当商品のID
     * @param multipartFile 登録する商品画像
     * @return item       Item
     */
    public Item uploadImagePath(
            Long id,
            MultipartFile multipartFile) throws IOException {

        // 商品を取得
        Item item = findById(id);
        if (item.getImagePath() != null) {
            deleteImageItem(item);
        }
        item.setImagePath(itemImageService.uploadImage(multipartFile));
        itemRepository.save(item);
        return item;
    }


    /**
     * 指定の画像データを返却。
     * 対象の商品が存在しない場合エラー処理。
     *
     * @param id 商品id
     * @return 画像データ HttpEntity<byte[]>
     */
    public HttpEntity<byte[]> getImageItem(Long id) {

        Item item = findById(id);
        if (item.getImagePath() == null) {
            throw new NotFoundException("画像が見つかりませんでした( 商品ID= " + id + ")");
        }

        byte[] bytes = itemImageService.getImage(imageSetting.getUploadDir() + item.getImagePath());

        HttpHeaders headers = new HttpHeaders();

        String contentType = URLConnection.guessContentTypeFromName(item.getImagePath());
        headers.setContentType(MediaType.valueOf(contentType));
        headers.setContentLength(bytes.length);

        return new ResponseEntity<>(bytes,headers, HttpStatus.OK);
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