package com.example.kawasakirestapi.domain.config;

import com.example.kawasakirestapi.domain.setting.FrontendSetting;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    private final FrontendSetting frontendSetting;

    public CorsConfig(FrontendSetting frontendSetting) {
        this.frontendSetting = frontendSetting;
    }

    @Bean
    public FilterRegistrationBean corsFilter() {

        CorsConfiguration config = new CorsConfiguration(); // CORS設定をチェックするメソッド
        config.setAllowCredentials(true); // ユーザ認証情報のサポート
        config.addAllowedOrigin(frontendSetting.getUrl()); // 許可するoriginを追加する
        config.addAllowedHeader(CorsConfiguration.ALL); // 許可するリクエストヘッダを追加する
        config.addAllowedMethod(CorsConfiguration.ALL); // 許可するHTTPメソッドを追加する

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config); // 接続を許可するパスを設定する

        FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // 登録Beanの順序を設定する
        return bean;
    }
}
