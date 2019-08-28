package com.example.kawasakirestapi.domain.setting;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OAuth認証関連の情報を管理
 */
@Component
@ConfigurationProperties(prefix = "setting.oauth")
@Getter
@Setter
public class OAuthSetting {

    //アクセストークンセッションキー
    private String accessTokenSessionKey;

}
