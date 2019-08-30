package com.example.kawasakirestapi.domain.interceptor;

import com.example.kawasakirestapi.application.exception.oauth.TokenNotFoundException;
import com.example.kawasakirestapi.application.exception.oauth.TokenTimeoutException;
import com.example.kawasakirestapi.domain.service.oauth.AuthenticationOauthService;
import com.example.kawasakirestapi.infrastructure.entity.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 *  トークンを取得して有効期限内か確認するインターセプト
 *
 * @author kawasakiryosuke
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    AuthenticationOauthService authenticationOauthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
        // 共通処理を記述する。
        String authToken = Optional.ofNullable(request.getHeader("authorization"))
                .orElseThrow(() -> new TokenNotFoundException("トークンがリクエストに含まれていません"));

             AuthenticationToken authenticationToken = authenticationOauthService.findByToken(authToken.replace("Bearer", "").trim()).orElseThrow(() -> new TokenNotFoundException("トークンがデータベースに登録されていません"));

             //トークンが有効期限切れだったら例外を返す
            if (authenticationToken.isExpired()) {
                throw new TokenTimeoutException();
            }

            return true;
    }

}