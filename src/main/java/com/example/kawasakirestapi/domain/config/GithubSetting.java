package com.example.kawasakirestapi.domain.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "setting.github")
@Getter
@Setter
public class GithubSetting {

    private String client;

    private String secret;

    private String callbackUrl;
}
