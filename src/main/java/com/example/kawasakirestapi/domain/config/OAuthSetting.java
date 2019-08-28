package com.example.kawasakirestapi.domain.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "setting.oauth")
@Getter
@Setter
public class OAuthSetting {

    //アクセストークンセッションキー
    private String accessTokenSessionKey;

}
