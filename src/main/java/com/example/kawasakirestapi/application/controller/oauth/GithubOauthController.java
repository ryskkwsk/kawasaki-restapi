package com.example.kawasakirestapi.application.controller.oauth;

import com.example.kawasakirestapi.application.exception.InvalidAuthorizeException;
import com.example.kawasakirestapi.domain.service.oauth.GithubOauthService;
import lombok.AllArgsConstructor;
import org.springframework.social.github.api.GitHub;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
public class GithubOauthController {

    private GithubOauthService oauthService;

    private static final String TOKEN = "token";

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
     * @return
     */
    @GetMapping("/github/login")
    public String login() {
        return "redirect:" + oauthService.getOauthAuthorizeUrl();
    }

    /**
     * githubから取得したプロフィールを表示。
     *
     * @param model Model
     * @return view
     */
    @GetMapping("github/profile")
    public String viewProfile(Model model)  {

        Object userInfo = httpSession.getAttribute(TOKEN);

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
     * @return githubのプロフィールへリダイレクト
     */
    @GetMapping("github/callback")
    public String getToken(@RequestParam("authenticationCode") String authenticationCode) {

        if (authenticationCode == null) {
            return "error/401";
        }

        String accessToken = oauthService.getAccessToken(authenticationCode);
        httpSession.setAttribute(TOKEN, accessToken);

        return "redirect:/github/profile";
    }

    /**
     * セッション破棄
     *
     * @return ログインページにリダイレクト
     */
    @GetMapping("github/logout")
    public String logout() {
        httpSession.invalidate();
        return "redirect:/";
    }

}
