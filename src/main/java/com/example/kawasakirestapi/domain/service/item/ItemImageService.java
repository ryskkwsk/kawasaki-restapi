package com.example.kawasakirestapi.domain.service.item;


import com.example.kawasakirestapi.application.exception.Item.InvalidImageFileException;
import com.example.kawasakirestapi.application.exception.Item.ItemImageException;
import com.example.kawasakirestapi.domain.service.aws.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemImageService {

    @Value("${setting.image.uploadDir}")
    private String uploadDir;

    private final AwsS3Service awsS3Service;

    /**
     * 画像のバリデーション
     *
     * @param multipartFile アップロードファイル
     * @return originalImageName オリジナルの画像名
     */
    private static String validateImage(MultipartFile multipartFile) throws IOException {

        String originalImageName = multipartFile.getOriginalFilename();
        byte[] originalImageByte = multipartFile.getBytes();

        // ファイルの形式チェック jpg, png, gif以外はnullを返す
        try(InputStream in = new ByteArrayInputStream(originalImageByte)) {
            String contentType = URLConnection.guessContentTypeFromStream(in);
            if (contentType == null || !contentType.matches("image/(jpeg|png|gif)")) {
                throw new InvalidImageFileException(originalImageName + "のファイル形式は許可されていません");
            }
        }
        // originalImageNameがnull → AssertionError
        assert originalImageName != null;
        return originalImageName;
    }

    /**
     * 指定(id値によって)の商品に対し画像を投稿。
     * 指定の商品が存在しない場合、例外処理。
     * 画像が正しい形式でない場合、例外処理
     *
     * @param multipartFile MultipartFile
     * @return item       Item
     */
    String uploadImage(MultipartFile multipartFile) throws IOException {

        // 画像のバリデーション
        String originalImageName = validateImage(multipartFile);
        UUID uuid = UUID.randomUUID();
        // 拡張子取得、識別子生成
        String extension = originalImageName.substring(originalImageName.lastIndexOf("."));
        String imagePath = uuid.toString()+ extension;

        InputStream inputStream;

        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            throw new ItemImageException("画像の読み込みに失敗しました", e);
        }
        awsS3Service.uploadS3(imagePath, inputStream, multipartFile);

        return imagePath;
    }


    void deleteFile(String imagePath) {
        File file = new File(uploadDir + imagePath);
        awsS3Service.deleteS3Object(file.toString());
    }

    /**
     *  画像を取得する
     *
     * @param resource resource
     * @return 画像
     */
    public byte[] getImage(String resource) {
        try {
            return IOUtils.toByteArray(awsS3Service.getS3Object(resource));
        } catch (IOException e) {
            throw new ItemImageException("画像の取得に失敗しました", e);
        }
    }

}
