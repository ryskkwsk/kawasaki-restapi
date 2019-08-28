package com.example.kawasakirestapi.application.controller.oauth;

import com.example.kawasakirestapi.application.exception.InvalidAuthorizeException;
import com.example.kawasakirestapi.domain.setting.OAuthSetting;
import com.example.kawasakirestapi.domain.service.oauth.GithubOauthService;
import lombok.AllArgsConstructor;
import org.springframework.social.github.api.GitHub;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 *  Githubによるソーシャルログインを行うコントローラー
 */
@Controller
@AllArgsConstructor
public class GithubOauthController {

    private OAuthSetting oAuthSetting;

    private GithubOauthService oauthService;

    private HttpSession httpSession;

    /**
     * ログインページを表示
     * @return ログインページのviewを返す
     */
    @GetMapping("/")
    public String index() {
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
    public String viewProfile(Model model)  {

        Object userInfo = httpSession.getAttribute(oAuthSetting.getAccessTokenSessionKey());

        if(userInfo == null) {
            throw new InvalidAuthorizeException("ユーザー認証が正しく行われておりません");
        }

        GitHub gitHub = oauthService.getGithub(userInfo);

        String userName = gitHub.userOperations().getUserProfile().getUsername();
        Long userId = gitHub.userOperations().getUserProfile().getId();

        model.addAttribute("userName", userName);
        model.addAttribute("userId", userId);


        return "oauth/github/index";
    }

    /**
     * github OAuthコールバック時のアクション
     *
     * @param authenticationCode String
     * @return viewProfileメソッドへリダイレクト
     */
    @GetMapping("/github/callback")
    public String getToken(@RequestParam("code") String authenticationCode) {

        if (authenticationCode == null) {
            return "error/401";
        }

        String accessToken = oauthService.getAccessToken(authenticationCode);
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
