package com.example.kawasakirestapi.application.controller.sessioninfo;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 *  セッション情報を格納するクラス
 *
 * @author kawasakiryosuke
 */
@SessionScope
@Component
@Getter
@Setter
public class TokenSessionInfo {

    private String accessToken;

    /**
     *  アクセストークンがnullかどうかチェック
     * @return trueかfalse
     */
    public boolean checkToken() {
        return accessToken != null;
    }

}
