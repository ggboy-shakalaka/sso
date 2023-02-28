package com.zhaizq.sso.controller;

import com.zhaizq.sso.sdk.SsoConstant;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.net.URI;
import java.util.stream.Stream;

@RestController
@RequestMapping("/uncheck")
public class UncheckController extends BaseController {
    @RequestMapping("/login")
    public String login() {
        response.addCookie(new Cookie("sso.token", "123456"));
        return "login success";
    }

    @RequestMapping("/doLogin")
    public String doLogin() {
        return "login page";
    }

    @RequestMapping("/logout")
    public String logout() {
        Cookie cookie = new Cookie("sso.token", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "logout success";
    }

    @RequestMapping("/refresh-token")
    public void refreshToken() throws IOException {
        Cookie[] cookies = request.getCookies();
        String token = cookies != null ? Stream.of(cookies).filter(v -> SsoConstant.TOKEN_NAME.equals(v.getName())).map(Cookie::getValue).findAny().orElse(null) : null;

        if (!checkToken(token)) {
            response.sendRedirect("/uncheck/doLogin");
            return;
        }

        URIBuilder uriBuilder = new URIBuilder(URI.create(request.getParameter("redirect")));
        uriBuilder.addParameter("source", request.getParameter("source"));
        uriBuilder.addParameter("sso.token", "123456");
        response.sendRedirect(uriBuilder.toString());
    }

    private boolean checkToken(String token) {
        return "123456".equals(token);
    }
}