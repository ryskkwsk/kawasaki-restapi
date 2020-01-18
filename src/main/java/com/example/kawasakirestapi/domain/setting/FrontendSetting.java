package com.example.kawasakirestapi.domain.setting;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * フロントエンドの情報を管理
 *
 * @author kawasakiryosuke
 */
@Component
@ConfigurationProperties(prefix = "setting.frontend")
@Data
public class FrontendSetting {
    // フロントエンドのURL
    private String url;
}
