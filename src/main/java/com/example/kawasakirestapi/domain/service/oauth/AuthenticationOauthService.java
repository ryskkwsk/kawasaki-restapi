package com.example.kawasakirestapi.domain.service.oauth;

import com.example.kawasakirestapi.application.exception.oauth.TokenNotFoundException;
import com.example.kawasakirestapi.domain.repository.oauth.AuthenticationRepository;
import com.example.kawasakirestapi.infrastructure.entity.oauth.AuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

/**
 * 認証トークンを扱うサービス
 *
 * @author kawasakiryosuke
 */
@Service
@RequiredArgsConstructor
public class AuthenticationOauthService {

    private final AuthenticationRepository authenticationRepository;

    /**
     * 認証情報をデータベースに保存
     */
    public void saveToken(
            String userName,
            Long userId,
            String authToken) {

        AuthenticationToken newToken = new AuthenticationToken();
        newToken.setUserId(userId);
        newToken.setUserName(userName);
        newToken.setAuthToken(authToken);

        authenticationRepository.save(newToken);
    }

    /**
     * 認証トークンの生成
     * @return 認証トークンを返す
     */
    public String generateToken() {

        String uuid = UUID.randomUUID().toString();
        return Base64.getEncoder().encodeToString(uuid.getBytes());
    }

    /**
     *
     * @param authToken
     * @param userProfile
     * @return
     */
    public boolean verifyAuthToken(String authToken, GitHubUserProfile userProfile) {
        // 認証用トークンがすでに登録されているか確認
        if (checkAuthTokenIsAlreadyRegistered(userProfile.getId())) {
            // すでに登録されているトークン情報を削除
            deleteAuthToken(getAuthToken(userProfile.getId()));
            // 新たに生成されたトークン情報をDBに保存
            saveToken(userProfile.getName(), userProfile.getId(), authToken);

        } else {
            // 認証情報をdbに保存
            saveToken(userProfile.getName(), userProfile.getId(), authToken);
        }
        return true;
    }


    /**
     * 認証用トークンを取得。
     *
     * @param userId Long
     * @return token   String
     */
    public String getAuthToken(Long userId) {

        return authenticationRepository.findByUserId(userId)
                .map(AuthenticationToken::getAuthToken)
                .orElseThrow(() -> new TokenNotFoundException("トークンがデータベースに登録されていません。"));
    }

    /**
     * 認証情報を削除
     *
     * @param token
     */
    public void deleteAuthToken(String token) {

        AuthenticationToken authToken = authenticationRepository.findByAuthToken(token).orElseThrow(() -> new TokenNotFoundException("トークンが見つかりません"));
        authenticationRepository.delete(authToken);

    }

    /**
     * すでに認証用のトークンが登録されているか確認。
     * 登録されている場合、trueを返す
     *
     * @param userId Long
     * @return token       String
     */
    public boolean checkAuthTokenIsAlreadyRegistered(Long userId) {

        Optional<AuthenticationToken> registeredToken = authenticationRepository.findByUserId(userId);
        return registeredToken.isPresent();
    }

    /**
     *  authTokenを取得
     *
     * @param authToken
     * @return 登録されているauthTokenの情報を返す
     */
    public Optional<AuthenticationToken> findByToken(String authToken) {
        return authenticationRepository.findByAuthToken(authToken);
    }




}