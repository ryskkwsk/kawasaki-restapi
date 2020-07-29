package com.example.kawasakirestapi.domain.setting;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 画像関連のapplication固有セッティング
 *
 * @author kawasakiryosuke
 */
@Component
@Data
@ConfigurationProperties(prefix = "setting.image")
public class ImageSetting {

    /** 画像アップロードディレクトリ */
    private String uploadDir;
}
