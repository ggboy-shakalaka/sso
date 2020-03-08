package cn.zhaizq.sso.sdk;

import cn.zhaizq.sso.sdk.domain.SsoConfig;
import cn.zhaizq.sso.sdk.domain.SsoConfig2;
import cn.zhaizq.sso.sdk.domain.SsoUser;
import cn.zhaizq.sso.sdk.domain.request.SsoCheckTokenRequest;
import cn.zhaizq.sso.sdk.domain.request.SsoLoginRequest;
import cn.zhaizq.sso.sdk.domain.request.SsoLogoutRequest;
import cn.zhaizq.sso.sdk.domain.response.SsoResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ggboy.framework.common.exception.InternalException;

import java.io.IOException;

public class SsoApi {
    private SsoConfig2 ssoConfig;

    public SsoApi(SsoConfig2 ssoConfig) throws InternalException, IOException {
        this.ssoConfig = ssoConfig;

        if (ssoConfig.isLocal()) {
            return;
        }

        SsoResponse<SsoConfig> response = queryConfig();

        if (response.code() != 200)
            throw new InternalException("单点登录服务初始化失败, message: " + response.message());

        ssoConfig.setServer1(response.data());
    }

    private SsoResponse<SsoConfig> queryConfig() throws IOException {
        String requestPath = ssoConfig.getServer() + "/api/" + ssoConfig.getAppId() + "/query_config";
        String response = doRequest(requestPath, null);
        return ResponseParser.parse(response, SsoConfig.class);
    }

    public SsoResponse<SsoUser> checkToken(String token) throws IOException {
        if (token == null) {
            return new SsoResponse<SsoUser>().code(400);
        }

        String requestPath = server + getSsoConfig().getCheckTokenPath();
        SsoCheckTokenRequest request = new SsoCheckTokenRequest();
        request.setToken(token);
        String response = doRequest(requestPath, request);
        return ResponseParser.parse(response, SsoUser.class);
    }

    public SsoResponse<String> login(SsoLoginRequest request) throws IOException {
        String requestPath = server + getSsoConfig().getLoginPath();
        String response = doRequest(requestPath, request);
        return ResponseParser.parse(response, String.class);
    }

    public SsoResponse<String> logout(String token) throws IOException {
        String requestPath = server + getSsoConfig().getLogoutPath();
        SsoLogoutRequest request = new SsoLogoutRequest();
        request.setSsoToken(token);
        String response = doRequest(requestPath, request);
        return ResponseParser.parse(response, String.class);
    }

    public SsoConfig getSsoConfig() {
        if (ssoConfig == null)
            return ssoConfig = initSsoConfig();
        return ssoConfig;
    }

    private String doRequest(String path, Object data) throws IOException {
        String request = data instanceof String ? (String) data : JSON.toJSONString(data);
        String response = null;

        try {
            return response = SsoHelper.doJsonRequest(path, request);
        } finally {
            // todo log
            System.out.println(request);
            System.out.println(response);
        }
    }

    public final static class ResponseParser {
        public static <T> SsoResponse<T> parse(String json, Class<T> clazz) {
            return JSON.parseObject(json, new TypeReference<SsoResponse<T>>() {
            });
        }
    }
}