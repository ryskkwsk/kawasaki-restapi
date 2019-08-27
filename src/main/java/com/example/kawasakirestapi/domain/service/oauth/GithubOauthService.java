package com.example.kawasakirestapi.domain.service.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.github.connect.GitHubConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;


@Service
public class GithubOauthService {

    @Value("${github.client}")
    private String client;

    @Value("${github.secret}")
    private String secret;

    @Value("${github.callbackUrl}")
    private String callbackUrl;

    /**
     * github認証URL生成
     * @return
     */
    public String getOauthAuthorizeUrl() {
        return operations().buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, new OAuth2Parameters());
    }

    /**
     * 認証コードをもとにアクセストークン取得
     * @param code
     * @return
     */
    public String getAccessToken(String code) {
        AccessGrant accessGrant = operations().exchangeForAccess(code, callbackUrl, null);
        return accessGrant.getAccessToken();
    }

    /**
     * githubの認証のコア機能
     * @return
     */
    private OAuth2Operations operations() {
        GitHubConnectionFactory gitHubConnectionFactory = new GitHubConnectionFactory(client, secret);
        return gitHubConnectionFactory.getOAuthOperations();
    }
}
