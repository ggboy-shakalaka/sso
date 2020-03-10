package cn.zhaizq.sso.sdk;

import cn.zhaizq.sso.sdk.domain.SsoConfig;
import cn.zhaizq.sso.sdk.domain.SsoUser;
import cn.zhaizq.sso.sdk.domain.request.SsoCheckTokenRequest;
import cn.zhaizq.sso.sdk.domain.request.SsoLoginRequest;
import cn.zhaizq.sso.sdk.domain.request.SsoLogoutRequest;
import cn.zhaizq.sso.sdk.domain.response.SsoResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.io.IOException;

public class SsoApi {
    private SsoConfig config;

    public SsoResponse<SsoUser> checkToken(String token) throws IOException {
        if (token == null) {
            return new SsoResponse<SsoUser>().code(400);
        }

        String response = doRequest(config.getCheckTokenUrl(), SsoCheckTokenRequest.build(token));
        return ResponseParser.parse(response, SsoUser.class);
    }

    public SsoResponse<String> login(SsoLoginRequest request) throws IOException {
        String response = doRequest(config.getLoginUrl(), request);
        return ResponseParser.parse(response, String.class);
    }

    public SsoResponse<String> logout(String token) throws IOException {
        String response = doRequest(config.getServerLogoutUrl(), SsoLogoutRequest.build(token));
        return ResponseParser.parse(response, String.class);
    }

    public SsoConfig getConfig() {
        return this.config;
    }

    public static SsoApi init(SsoConfig config) throws IOException {
        SsoApi ssoApi = new SsoApi();

        if (!config.isLocal()) {
            String responseStr = doRequest(config.getQueryConfigUrl(), null);
            SsoResponse<SsoConfig.ServerConfig> response = ResponseParser.parse(responseStr, SsoConfig.ServerConfig.class);

            if (response.getCode() == 200) {
                config.setServerConfig(response.getData());
            } else {
                throw new IOException("单点登录服务初始化失败, message: " + response.getMessage());
            }
        }

        ssoApi.config = config;
        return ssoApi;
    }

    private static String doRequest(String path, Object data) throws IOException {
        String request = data instanceof String ? (String) data : JSON.toJSONString(data);
        String response = null;
        long time = System.currentTimeMillis();

        try {
            return response = SsoHelper.doJsonRequest(path, request);
        } finally {
            // todo log
            System.out.println(request);
            System.out.println(response);
            System.out.println(System.currentTimeMillis() - time);
        }
    }

    final static class ResponseParser {
        static <T> SsoResponse<T> parse(String json, Class<T> clazz) {
            return JSON.parseObject(json, new TypeReference<SsoResponse<T>>() {});
        }
    }
}