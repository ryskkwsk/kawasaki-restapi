package com.example.kawasakirestapi.domain.config;

import com.example.kawasakirestapi.domain.setting.FrontendSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


/**
 * CORS用の設定ファイル
 *
 * @author kawasakiryosuke 
 */
@Configuration
@RequiredArgsConstructor
public class CorsConfig {

    private final FrontendSetting frontendSetting;

    @Bean
    public FilterRegistrationBean corsFilter() {

        // CORS設定をチェックするメソッド
        CorsConfiguration config = new CorsConfiguration();
        // ユーザ認証情報のサポート
        config.setAllowCredentials(true);
        // 許可するoriginを追加する
        config.addAllowedOrigin(frontendSetting.getUrl());
        // 許可するリクエストヘッダを追加する
        config.addAllowedHeader(CorsConfiguration.ALL);
        // 許可するHTTPメソッドを追加する
        config.addAllowedMethod(CorsConfiguration.ALL);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 接続を許可するパスを設定する
        source.registerCorsConfiguration(frontendSetting.getAllowPath(), config);

        FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(source));
        // 登録Beanの順序を設定する
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
