package com.zhaizq.sso.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhaizq.sso.sdk.domain.SsoConfig;
import com.zhaizq.sso.sdk.domain.SsoRequest;
import com.zhaizq.sso.sdk.domain.SsoResponse;
import com.zhaizq.sso.sdk.domain.SsoServerConfig;
import com.zhaizq.sso.sdk.domain.request.SsoLoginRequest;
import lombok.Getter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
public class SsoService {
    private final SsoConfig ssoConfig;
    private final SsoServerConfig serverConfig;

    public SsoService(SsoConfig ssoConfig) {
        this.ssoConfig = ssoConfig;
        this.serverConfig = new SsoServerConfig();
    }

    public SsoServerConfig queryConfig() throws Exception {
        SsoResponse response = this.request("query_config");
        return response.getData() != null ? JSON.parseObject(response.getData().toString(), SsoServerConfig.class) : null;
    }


    public SsoResponse helloWorld() throws Exception {
        return this.request(SsoConstant.Method.HELLO_WORLD, null);
    }

    public SsoResponse refreshToken(String name) throws Exception {
        return null;
    }

    public String getToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        Cookie cookie = Stream.of(request.getCookies()).filter(v -> SsoConstant.TOKEN_NAME.equals(v.getName())).findAny().orElse(null);
        return cookie == null ? null : cookie.getValue();
    }

    public SsoResponse checkToken(String token) throws IOException {
        this.request("", null);

        if (token == null) return SsoResponse.build(400, null, null);

        String s = HttpUtil.postJson(ssoConfig.getServer() + "/api/checkToken", "token=" + token);
        return JSON.parseObject(s, SsoResponse.class);
    }

    public SsoResponse checkToken(HttpServletRequest request) throws IOException {
        String token = getToken(request);
        return this.request("check_token", new JSONObject().fluentPut("token", token));
    }

    public String buildRedirectUrl(String redirect) throws UnsupportedEncodingException {
        return ssoConfig.getServer() + "/uncheck/refresh?appKey=" + ssoConfig.getApp() + "&redirect=" + URLEncoder.encode(redirect, "UTF-8");
    }

    public SsoResponse queryLoginPublicKey(String name) throws Exception {
        return null;
    }

    public SsoResponse login(SsoLoginRequest request) throws Exception {
        return this.request(SsoConstant.Method.LOGIN, JSON.toJSONString(request));
    }

    public SsoResponse logout(String token) throws Exception {
        return this.request(SsoConstant.Method.CHECK_TOKEN, new JSONObject().fluentPut("token", token).toJSONString());
    }

    public boolean isNotMatch(String path) {
        for (String ignore : ssoConfig.getIgnore())
            if (SsoHelper.isMatch(ignore, path))
                return true;

        return false;
    }

    public boolean isMatchSetToken(String path) {
        return SsoHelper.isMatch(ssoConfig.getSetToken(), path);
    }

    private SsoServerConfig getServerConfig() {
        if (!serverConfig.isInit()) {

        }

        serverConfig.setInit(true);
        return serverConfig;
    }

    private SsoResponse request(String method) {
        return request(method, null);
    }

    private SsoResponse request(String method, Object params) {
        try {
            SsoRequest ssoRequest = new SsoRequest();
            ssoRequest.setApp(ssoConfig.getApp());
            ssoRequest.setUuid(UUID.randomUUID().toString());
            ssoRequest.setMethod(method);
            ssoRequest.setTimestamp(System.currentTimeMillis());
            ssoRequest.setParams(params);

            String body = JSON.toJSONString(ssoRequest);
            String sign = StringRsaUtil.sign(body, ssoConfig.getPrivateKey());
            String url = ssoConfig.getServer() + "?sign=" + sign;

            long time = System.currentTimeMillis();
            System.out.printf("[APP -> SSO] url: %s, body: %s%n", url, body);
            String result = this.doRequest(url, body);
            System.out.printf("[APP <- SSO] result: %s, time: %d(ms)%n", result, System.currentTimeMillis() - time);
            return JSON.parseObject(result, SsoResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String doRequest(String url, String body) throws IOException {
        return HttpUtil.postJson(url, body);
    }

    protected void doOnTokenExpire(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String redirect = ssoConfig.getHost() + ssoConfig.getSetToken();

        Map<String, Object> map = new HashMap<>();
        map.put("code", 302);
        map.put("path", serverConfig.getRefreshTokenUrl());
        map.put("redirect", redirect);
        map.put("sign", StringRsaUtil.sign(redirect, ssoConfig.getPrivateKey()));
        response.getWriter().write(JSON.toJSONString(map));
    }
}