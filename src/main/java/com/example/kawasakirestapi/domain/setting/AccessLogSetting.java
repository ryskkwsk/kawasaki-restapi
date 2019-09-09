package com.example.kawasakirestapi.domain.setting;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * AccessLogの設定クラス
 *
 * @author kawasakiryosuke
 */
@Component
@ConfigurationProperties(prefix = "setting.log")
@Getter
@Setter
public class AccessLogSetting {

    private String accessLogPath;

    private String datetimeFormat;
}
