package cn.zhaizq.sso.web.controller.abc;

import cn.zhaizq.sso.sdk.SsoConstant;
import cn.zhaizq.sso.sdk.SsoHelper;
import cn.zhaizq.sso.sdk.domain.SsoConfig;
import cn.zhaizq.sso.sdk.domain.SsoUser;
import cn.zhaizq.sso.sdk.domain.request.SsoCheckTokenRequest;
import cn.zhaizq.sso.sdk.domain.request.SsoLoginRequest;
import cn.zhaizq.sso.sdk.domain.request.SsoLogoutRequest;
import cn.zhaizq.sso.sdk.domain.response.SsoResponse;
import cn.zhaizq.sso.service.domain.entry.User;
import cn.zhaizq.sso.service.service.LoginService;
import cn.zhaizq.sso.web.cache.TokenCache;
import cn.zhaizq.sso.web.cache.UserCache;
import cn.zhaizq.sso.web.controller.BaseController;
import com.ggboy.framework.common.exception.BusinessException;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/api/{appId}")
public class ApiController extends BaseController {
    @Autowired
    private SsoConfig ssoConfig;
    @Autowired
    private UserCache userCache;
    @Autowired
    private TokenCache tokenCache;
    @Autowired
    private LoginService loginService;

    @PostMapping("/query_config")
    public SsoResponse<SsoConfig.ServerConfig> query_config(@PathVariable String appId) {
        SsoConfig.ServerConfig config = new SsoConfig.ServerConfig();
        config.setServerUrl(ssoConfig.getServerConfig().getServerUrl());
        config.setLoginPath(String.format("/api/%s/login", appId));
        config.setLogoutPath(String.format("/api/%s/logout", appId));
        config.setCheckTokenPath(String.format("/api/%s/check_token", appId));
        config.setRefreshTokenPath(String.format("/api/%s/refresh_token", appId));

        return new SsoResponse<SsoConfig.ServerConfig>().code(200).data(config);
    }

    @PostMapping("/check_token")
    public SsoResponse<SsoUser> check_token(@PathVariable String appId, @RequestBody SsoCheckTokenRequest ssoCheckToken) {
        User user = userCache.get(ssoCheckToken.getToken());
        if (user == null)
            return new SsoResponse<SsoUser>().code(400);

        SsoUser ssoUser = new SsoUser();
        ssoUser.setId(user.getId() + "");
        ssoUser.setName(user.getUserName());
        return new SsoResponse<SsoUser>().code(200).data(ssoUser);
    }

    @GetMapping("/refresh_token")
    public void refresh_token(@PathVariable String appId, @RequestParam String redirect, @RequestParam String login_url) throws IOException {
        String token = SsoHelper.getSsoToken(request);

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
        uri.addParameter("appId", appId);
        response.sendRedirect(uri.toString());
    }

    @PostMapping("/login")
    @ResponseBody
    public SsoResponse<String> login(@PathVariable String appId, @RequestBody SsoLoginRequest loginRequest) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, IOException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        User user = loginService.login(loginRequest.getName(), loginRequest.getPassword());

        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        String ssoToken = tokenCache.get(loginRequest.getName());
        if (ssoToken == null) {
            tokenCache.put(loginRequest.getName(), ssoToken = UUID.randomUUID().toString());
        }

        userCache.put(ssoToken, user);

        return new SsoResponse<String>().code(200).data(ssoToken);
    }

    @PostMapping("/logout")
    public SsoResponse<?> logout(@RequestBody SsoLogoutRequest request) {
        userCache.put(request.getToken(), null);
        return new SsoResponse<>().code(200);
    }

    @GetMapping("/getPublicKey")
    public SsoResponse<String> getPublicKey(@RequestParam String name) throws NoSuchAlgorithmException {
        String publicKey = loginService.getPublicKeyByName(name);
        return new SsoResponse<String>().code(200).data(publicKey);
    }
}