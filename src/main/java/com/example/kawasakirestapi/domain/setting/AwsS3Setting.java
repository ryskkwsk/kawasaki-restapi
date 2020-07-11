package com.example.kawasakirestapi.domain.setting;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * AWS S3設定
 */
@Component
@Data
@ConfigurationProperties(prefix = "aws")
public class AwsS3Setting {

    /** S3リージョン */
    private String region;

    /** S3アクセスキー */
    private String accessKey;

    /** S3シークレットキー */
    private String secretKey;

    /** S3バケット */
    private String s3bucket;

    /** S3画像保存ディレクトリ */
    private String imageDir;
}
