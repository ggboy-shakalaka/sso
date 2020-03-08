package cn.zhaizq.sso.web.controller.abc;

import cn.zhaizq.sso.sdk.SsoConstant;
import cn.zhaizq.sso.sdk.SsoHelper;
import cn.zhaizq.sso.sdk.domain.SsoConfig;
import cn.zhaizq.sso.sdk.domain.SsoUser;
import cn.zhaizq.sso.sdk.domain.request.SsoCheckTokenRequest;
import cn.zhaizq.sso.sdk.domain.request.SsoLoginRequest;
import cn.zhaizq.sso.sdk.domain.request.SsoLogoutRequest;
import cn.zhaizq.sso.sdk.domain.response.SsoCheckTokenResponse;
import cn.zhaizq.sso.sdk.domain.response.SsoLoginResponse;
import cn.zhaizq.sso.sdk.domain.response.SsoLogoutResponse;
import cn.zhaizq.sso.sdk.domain.response.SsoQueryConfigResponse;
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
import javax.servlet.http.Cookie;
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
    private UserCache userCache;
    @Autowired
    private TokenCache tokenCache;
    @Autowired
    private LoginService loginService;

    @Value("${sso.base.location}")
    private String baseLocation;

    @PostMapping("/query_config")
    public SsoQueryConfigResponse query_config(@PathVariable String appId) {
        SsoConfig config = new SsoConfig();
        config.setServerPath(baseLocation);
        config.setLoginPath(String.format("/api/%s/login", appId));
        config.setLogoutPath(String.format("/api/%s/logout", appId));
        config.setCheckTokenPath(String.format("/api/%s/check_token", appId));
        config.setRefreshTokenPath(String.format("/api/%s/refresh_token", appId));

        SsoQueryConfigResponse response = new SsoQueryConfigResponse();
        response.setCode("200");
        response.setData(config);
        return response;
    }

    @PostMapping("/check_token")
    public SsoCheckTokenResponse check_token(@PathVariable String appId, @RequestBody SsoCheckTokenRequest ssoCheckToken) {
        SsoCheckTokenResponse response = new SsoCheckTokenResponse();

        User user = userCache.get(ssoCheckToken.getToken());
        if (user == null) {
            response.setCode("400");
            return response;
        }

        SsoUser ssoUser = new SsoUser();
        ssoUser.setId(user.getId() + "");
        ssoUser.setName(user.getUserName());

        response.setCode("200");
        response.setData(ssoUser);
        return response;
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
    public SsoLoginResponse login(@PathVariable String appId, @RequestBody SsoLoginRequest loginRequest) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, IOException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {
        SsoLoginResponse response = new SsoLoginResponse();

        User user = loginService.login(loginRequest.getName(), loginRequest.getPassword());

        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        String ssoToken = tokenCache.get(loginRequest.getName());
        if (ssoToken == null) {
            tokenCache.put(loginRequest.getName(), ssoToken = UUID.randomUUID().toString());
        }

        userCache.put(ssoToken, user);
        response.setCode("200");
        response.setData(ssoToken);
        return response;
    }

    @PostMapping("/logout")
    public SsoLogoutResponse logout(@RequestBody SsoLogoutRequest request) {
        userCache.put(request.getSsoToken(), null);
        return new SsoLogoutResponse();
    }

    @GetMapping("/getPublicKey")
    public String getPublicKey(@RequestParam String name) throws NoSuchAlgorithmException {
        return loginService.getPublicKeyByName(name);
    }
}