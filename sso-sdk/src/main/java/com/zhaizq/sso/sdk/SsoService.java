package com.zhaizq.sso.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaizq.sso.sdk.domain.SsoConfig;
import com.zhaizq.sso.sdk.domain.request.SsoLoginRequest;
import com.zhaizq.sso.sdk.domain.response.SsoResponse;
import lombok.AllArgsConstructor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.stream.Stream;

@AllArgsConstructor
public class SsoService {
    private final SsoConfig ssoConfig;
    private final SsoClient ssoClient = new SsoClient();;

    public SsoResponse helloWorld() throws Exception {
        return ssoClient.request(SsoConstant.Method.HELLO_WORLD, null);
    }

    public SsoResponse refreshToken(String name) throws Exception {
        return null;
    }

    public String getToken(HttpServletRequest request) {
        Cookie cookie = Stream.of(request.getCookies()).filter(v -> SsoConstant.TOKEN_NAME.equals(v.getName())).findAny().orElse(null);
        return cookie == null ? null : cookie.getValue();
    }

    public SsoResponse checkToken(String token) throws IOException {
        if (token == null) return SsoResponse.build(400, null, null);

        String s = HttpUtil.doPost(ssoConfig.getServer() + "/api/checkToken", "token=" + token);
        return JSON.parseObject(s, SsoResponse.class);
    }

    public SsoResponse checkToken(HttpServletRequest request) throws IOException {
        return checkToken(getToken(request));
    }

    public String buildRedirectUrl(String redirect) throws UnsupportedEncodingException {
        return ssoConfig.getServer() + "/uncheck/refresh?appKey=" + ssoConfig.getAppId() + "&redirect=" + URLEncoder.encode(redirect, "UTF-8");
    }

    public SsoResponse queryLoginPublicKey(String name) throws Exception {
        return null;
    }

    public SsoResponse login(SsoLoginRequest request) throws Exception {
        return ssoClient.request(SsoConstant.Method.LOGIN, JSON.toJSONString(request));
    }

    public SsoResponse logout(String token) throws Exception {
        return ssoClient.request(SsoConstant.Method.CHECK_TOKEN, new JSONObject().fluentPut("token", token).toJSONString());
    }

    public boolean isMatchIgnore(String path) {
        for (String ignoreUrl : ssoConfig.getIgnore())
            if (SsoHelper.isMatch(ignoreUrl, path))
                return true;

        return false;
    }
}