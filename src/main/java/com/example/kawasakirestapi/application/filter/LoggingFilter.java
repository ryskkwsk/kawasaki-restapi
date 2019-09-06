package com.example.kawasakirestapi.application.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;


/**
 * APIリクエスト情報をキャッチしてログを書き込むクラス
 *
 * @author kawasakiryosuke
 */
@Slf4j
@Component
public class LoggingFilter implements Filter {

    /**
     * リクエスト情報をログに書き込む
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // HttpServletRequest にキャスト
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //URI取得
        String url = httpServletRequest.getRequestURI();

        if (!url.contains("api/items")) {
            chain.doFilter(request, response);
            return;
        }

        // currentTimeMills = 現在の時間をミリ秒単位で返す
        long startTime = System.currentTimeMillis();
        // メソッド取得
        String method = httpServletRequest.getMethod();
        chain.doFilter(request, response);
        // リクエスト処理にかかった時間
        long responseTime = System.currentTimeMillis() - startTime;
        // ステータスコード
        int statusCode = httpServletResponse.getStatus();

        log.info("{} {} {} {}",
                method,
                url,
                statusCode,
                responseTime);
    }

    @Override
    public void destroy() {
    }

}
