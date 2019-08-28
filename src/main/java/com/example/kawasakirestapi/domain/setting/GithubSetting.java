package com.example.kawasakirestapi.domain.setting;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Githubのプライベート情報を管理
 *
 * @author kawasakiryosuke
 */
@Component
@ConfigurationProperties(prefix = "setting.github")
@Getter
@Setter
public class GithubSetting {

    private String client;

    private String secret;

    private String callbackUrl;
}
