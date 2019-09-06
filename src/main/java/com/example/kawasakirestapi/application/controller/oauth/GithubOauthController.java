package com.example.kawasakirestapi.application.controller.oauth;

import com.example.kawasakirestapi.application.controller.sessioninfo.TokenSessionInfo;
import com.example.kawasakirestapi.application.exception.oauth.TokenNotFoundException;
import com.example.kawasakirestapi.domain.service.oauth.AuthenticationOauthService;
import com.example.kawasakirestapi.domain.service.oauth.GithubOauthService;
import com.example.kawasakirestapi.domain.setting.OAuthSetting;
import com.example.kawasakirestapi.infrastructure.entity.oauth.AuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *  Githubによるソーシャルログインを行うコントローラー
 *
 *  @author kawasakiryosuke
 */
@Controller
@RequiredArgsConstructor
public class GithubOauthController {

    private final OAuthSetting oAuthSetting;

    private final GithubOauthService oauthService;

    private final AuthenticationOauthService authenticationOauthService;

    private final HttpSession httpSession;

    private final TokenSessionInfo tokenSessionInfo;


    /**
     * ログインページを表示
     * @return ログインページのviewを返す
     */
    @GetMapping("/")
    public String index() {
        if(tokenSessionInfo.checkToken()){
            return "github/profile";
        }
        return "oauth/login";
    }

    /**
     * githubの認証画面へリダイレクト
     * @return github認証画面へリダイレクト
     */
    @GetMapping("/github/login")
    public String login() {
        return "redirect:" + oauthService.getOauthAuthorizeUrl();
    }

    /**
     * githubから取得したプロフィールを表示。
     *
     * @param model Model
     * @return githubのプロフィール画面を返す
     */
    @GetMapping("/github/profile")
    public String viewProfile(Model model, HttpServletResponse response) {

        if(!tokenSessionInfo.checkToken()){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return "error/401";
        }
        GitHub gitHub = oauthService.getGithub(httpSession.getAttribute(oAuthSetting.getAccessTokenSessionKey()));

        // ユーザープロフィール取得
        GitHubUserProfile userProfile = gitHub.userOperations().getUserProfile();

        // 認証用トークンを取得
        String authToken = authenticationOauthService.generateToken();

        // 認証トークン登録されているかチェック、されてた場合削除して更新
        authenticationOauthService.verifyAuthToken(authToken, userProfile);

        // 新しい認証トークンで登録された認証情報を取得
        AuthenticationToken authenticationToken = authenticationOauthService.findByToken(authToken).orElseThrow(() -> new TokenNotFoundException("トークンがデータベースに登録されていません"));

        // viewに渡す
        model.addAttribute("authenticationToken", authenticationToken);

        return "/oauth/github/profile";
    }

    /**
     * github OAuthコールバック時のアクション
     *
     * @param authenticationCode String
     * @return viewProfileメソッドへリダイレクト
     */
    @GetMapping("/github/callback")
    public String githubCallback(@RequestParam("code") String authenticationCode, HttpServletResponse response) {

        if (authenticationCode == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return "error/401";
        }
        // アクセストークン取得
        String accessToken = oauthService.getAccessToken(authenticationCode);
        // アクセストークンをsessioninfoに格納
        tokenSessionInfo.setAccessToken(accessToken);
        httpSession.setAttribute(oAuthSetting.getAccessTokenSessionKey(), accessToken);

        return "redirect:/github/profile";
    }

    /**
     * セッション破棄
     *
     * @return ログインページへリダイレクト
     */
    @GetMapping("/github/logout")
    public String logout() {
        httpSession.invalidate();
        return "redirect:/";
    }

}
