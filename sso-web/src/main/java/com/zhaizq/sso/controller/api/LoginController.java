package com.zhaizq.sso.controller.api;

import com.zhaizq.sso.common.cache.TokenCache;
import com.zhaizq.sso.common.cache.UserCache;
import com.zhaizq.sso.controller.BaseController;
import com.zhaizq.sso.common.exception.BusinessException;
import com.zhaizq.sso.service.domain.entry.User;
import com.zhaizq.sso.service.service.LoginService;
import com.zhaizq.sso.sdk.SsoConstant;
import com.zhaizq.sso.sdk.SsoHelper;
import com.zhaizq.sso.sdk.domain.request.SsoLoginRequest;
import com.zhaizq.sso.sdk.domain.response.SsoResponse;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@RestController
@RequestMapping("/api/login")
public class LoginController extends BaseController {
    @Autowired
    private UserCache userCache;
    @Autowired
    private TokenCache tokenCache;
    @Autowired
    private LoginService loginService;

    @GetMapping("/refresh_token")
    public void refresh_token(@RequestParam String app_id, @RequestParam String redirect, @RequestParam String login_url) throws IOException {
        String token = SsoHelper.getSsoToken(request).getValue();

        if (token != null && userCache.get(token) != null) {
            URIBuilder uri = new URIBuilder(URI.create(redirect));
            uri.addParameter(SsoConstant.TOKEN_NAME, token);
            response.sendRedirect(uri.toString());
            return;
        }

        if (login_url != null && login_url.length() > 0) {
            response.sendRedirect(login_url);
            return;
        }

        URIBuilder uri = new URIBuilder(URI.create("/api/login.html"));
        uri.addParameter(SsoConstant.REDIRECT, redirect);
        uri.addParameter("appId", app_id);
        response.sendRedirect(uri.toString());
    }

    @GetMapping("/public_key")
    public SsoResponse public_key(@RequestParam String name) throws NoSuchAlgorithmException {
        String publicKey = loginService.getPublicKeyByName(name);
        return new SsoResponse().code(200).data(publicKey);
    }

    @PostMapping("/do_login")
    @ResponseBody
    public SsoResponse do_login(@PathVariable String appId, @RequestBody SsoLoginRequest loginRequest) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, IOException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        User user = loginService.login(loginRequest.getName(), loginRequest.getPassword());

        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        String ssoToken = tokenCache.get(loginRequest.getName());
        if (ssoToken == null) {
            tokenCache.put(loginRequest.getName(), ssoToken = UUID.randomUUID().toString());
        }

        userCache.put(ssoToken, user);

        return new SsoResponse().code(200).data(ssoToken);
    }
}