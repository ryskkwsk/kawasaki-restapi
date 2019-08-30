package com.example.kawasakirestapi.domain.config;

import com.example.kawasakirestapi.domain.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * AuthorisationInterceptor用の設定ファイル
 *
 * @author kawasakiryosuke
 */
@Configuration
public class AuthenticationConfig implements WebMvcConfigurer {

    /**
     * interceptorをbeanに登録
     */
    @Bean
    public AuthenticationInterceptor authenticationIntercept() {
        return new AuthenticationInterceptor();
    }
    /**
     * intercptorのbeanをurlリクエストパターンに追加
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationIntercept())
                .addPathPatterns("/api/items/**");
    }
}
