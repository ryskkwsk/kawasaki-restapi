package com.example.kawasakirestapi.application.controller.oauth;

import com.example.kawasakirestapi.application.exception.InvalidAuthorizeException;
import com.example.kawasakirestapi.domain.service.oauth.GithubOauthService;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.impl.GitHubTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class GithubOauthController {

    private final GithubOauthService oauthService;

    private static final String TOKEN = "token";

    private final HttpSession httpSession;

    public GithubOauthController(
            GithubOauthService oauthService,
            HttpSession httpSession) {

        this.oauthService = oauthService;
        this.httpSession = httpSession;
    }

    @GetMapping("/")
    public String index() {
        return "oauth/login";
    }

    @GetMapping("/github/login")
    public String login() {
        return "redirect:" + oauthService.getOauthAuthorizeUrl();
    }

    /**
     * viewを表示。
     *
     * @param model Model
     * @return view
     */
    @GetMapping("github/profile")
    public String viewProfile(Model model)  {

        Object userInfo = httpSession.getAttribute(TOKEN);

        if(Objects.isNull(userInfo)) {
            throw new InvalidAuthorizeException("ユーザー認証が正しく行われておりません");
        }

        GitHub gitHub = new GitHubTemplate(userInfo.toString());

        String userName = gitHub.userOperations().getUserProfile().getUsername();
        Long userId = gitHub.userOperations().getUserProfile().getId();

        model.addAttribute("userName", userName);
        model.addAttribute("userId", userId);


        return "oauth/github/index";
    }

    /**
     * アクセストークンセッションに保存
     *
     * @param code String
     * @return redirect
     */
    @GetMapping("github/callback")
    public String getToken(@RequestParam String code) {

        if (code == null) {
            return "error/401";
        }

        String accessToken = oauthService.getAccessToken(code);
        httpSession.setAttribute(TOKEN, accessToken);

        return "redirect:/github/profile";
    }

    /**
     * セッション破棄
     *
     * @return redirect
     */
    @GetMapping("github/logout")
    public String logout() {
        httpSession.removeAttribute(TOKEN);
        return "redirect:/";
    }

}
