package com.example.kawasakirestapi.domain.service.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.example.kawasakirestapi.application.exception.Item.ItemImageException;
import com.example.kawasakirestapi.domain.setting.AwsS3Setting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * AWSアクセスサービス
 *
 * @author kawasakiryosuke
 */
@Service
@RequiredArgsConstructor
public class AwsS3Service {
    /** AmazonS3 */
    private final AmazonS3 amazonS3;

    /** 画像関連設定 */
    private final AwsS3Setting awsS3Setting;

    /**
     * s3へ画像のアップロード
     *
     * @param imagePath 画像パス
     * @param inputStream InputStream
     * @param multipartFile MultipartFile
     */
    public void uploadS3(String imagePath, InputStream inputStream, MultipartFile multipartFile) {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(
                    awsS3Setting.getS3bucket(),
                    awsS3Setting.getImageDir() + imagePath,
                    inputStream,
                    objectMetadata
            );
        } catch (Exception e) {
            throw new ItemImageException("画像の投稿に失敗しました", e);
        }
    }

    /**
     * s3から取得するためのオブジェクト
     *
     * @param resource
     * @return
     */
    public InputStream getS3Object(String resource) {
        try {
            S3Object s3Object = amazonS3.getObject(awsS3Setting.getS3bucket(), resource);
            return s3Object.getObjectContent();
        } catch (Exception e) {
            throw new ItemImageException("画像の取得に失敗しました", e);
        }
    }

    /**
     * s3から削除するためのオブジェクト
     *
     *
     * @param imageName
     */
    public void deleteS3Object(String imageName) {
        try {
            amazonS3.deleteObject(awsS3Setting.getS3bucket(), imageName);
        } catch (Exception e) {
            throw new ItemImageException("画像の削除に失敗しました", e);
        }
    }

}
