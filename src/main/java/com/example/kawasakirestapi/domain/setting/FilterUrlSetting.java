package com.example.kawasakirestapi.domain.setting;

import com.example.kawasakirestapi.application.filter.ApiLogFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterUrlSetting {

    private final ApiLogFilter apiLogFilter;

    @Bean
    public FilterRegistrationBean urlFilter(){
        // FilterをnewしてFilterRegistrationBeanのコンストラクタに渡す
        FilterRegistrationBean bean = new FilterRegistrationBean(apiLogFilter);
        // Filterのurl-patternを指定（可変長引数なので複数指定可能）
        bean.addUrlPatterns("api/items");
        return bean;
    }

}
