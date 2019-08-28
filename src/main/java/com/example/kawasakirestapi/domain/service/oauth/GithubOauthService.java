package com.example.kawasakirestapi.domain.service.oauth;

import com.example.kawasakirestapi.domain.setting.GithubSetting;
import lombok.AllArgsConstructor;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.impl.GitHubTemplate;
import org.springframework.social.github.connect.GitHubConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

/**
 * Githubによるソーシャルログインを行うサービス
 */
@Service
@AllArgsConstructor
public class GithubOauthService {

    private GithubSetting githubSetting;

    /**
     * github認証URL生成
     * @return 認証URL
     */
    public String getOauthAuthorizeUrl() {
        return getOAuth2Operations().buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, new OAuth2Parameters());
    }

    /**
     * 認証コードをもとにアクセストークン取得
     * @param authenticationCode　callbackで受け取ったgithubの認証コード
     * @return アクセストークン
     */
    public String getAccessToken(String authenticationCode) {
        AccessGrant accessGrant = getOAuth2Operations().exchangeForAccess(authenticationCode, githubSetting.getCallbackUrl(), null);
        return accessGrant.getAccessToken();
    }

    /**
     * githubの認証のコア機能
     * @return github認証情報
     */
    private OAuth2Operations getOAuth2Operations() {
        GitHubConnectionFactory gitHubConnectionFactory = new GitHubConnectionFactory(githubSetting.getClient(), githubSetting.getSecret());
        return gitHubConnectionFactory.getOAuthOperations();
    }

    /**
     * githubのユーザー情報取得
     * @param userInfo ユーザー情報
     * @return githubのユーザー情報
     */
    public GitHub getGithub(Object userInfo) {
        return new GitHubTemplate(userInfo.toString());
    }
}
