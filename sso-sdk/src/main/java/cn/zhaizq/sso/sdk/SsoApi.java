package cn.zhaizq.sso.sdk;

import cn.zhaizq.sso.sdk.domain.SsoConfig;
import cn.zhaizq.sso.sdk.domain.SsoUser;
import cn.zhaizq.sso.sdk.domain.request.*;
import cn.zhaizq.sso.sdk.domain.response.SsoResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ggboy.framework.utils.common.StringRsaUtil;
import com.ggboy.framework.utils.httputil.StringSimpleHttp;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class SsoApi {
    private SsoConfig config;

    public SsoApi(SsoConfig config) throws Exception {
        this.config = config;

        if (config.isLocal())
            return;

        String responseStr = doRequest(SsoConstant.Method.QUERY_CONFIG, null);
        SsoResponse<SsoConfig.ServerConfig> response = ResponseParser.parse(responseStr, SsoConfig.ServerConfig.class);

        if (response.getCode() != 200)
            throw new IOException("单点登录服务初始化失败, message: " + response.getMessage());

        config.setServerConfig(response.getData());
    }

    public SsoResponse<SsoUser> checkToken(String token) throws Exception {
        String response = doRequest(SsoConstant.Method.CHECK_TOKEN, new SsoCheckTokenRequest(token));
        return ResponseParser.parse(response, SsoUser.class);
    }

    public SsoResponse<String> queryPublicKey(String name) throws Exception {
        String response = doRequest(SsoConstant.Method.QUERY_LOGIN_PUBLIC_KEY, new SsoQueryLoginPublicKeyRequest(name));
        return ResponseParser.parse(response, String.class);
    }

    public SsoResponse<String> login(SsoLoginRequest request) throws Exception {
        String response = doRequest(SsoConstant.Method.CHECK_TOKEN, request);
        return ResponseParser.parse(response, String.class);
    }

    public SsoResponse<String> logout(String token) throws Exception {
        String response = doRequest(SsoConstant.Method.CHECK_TOKEN, SsoLogoutRequest.build(token));
        return ResponseParser.parse(response, String.class);
    }

    public SsoConfig getConfig() {
        return this.config;
    }

    private String doRequest(String method, Object data) throws Exception {
        long time = System.currentTimeMillis();
        String request = data instanceof String ? (String) data : JSON.toJSONString(data);
        String response = null;

        String sign = StringRsaUtil.sign(time + request, config.getPrivateKey());

        SsoRequestHeader ssoRequestHeader = new SsoRequestHeader();
        ssoRequestHeader.setApp_id(config.getAppId());
        ssoRequestHeader.setMethod(method);
        ssoRequestHeader.setTimestamp(time);
        ssoRequestHeader.setSign(sign);

        try {
            return response = StringSimpleHttp
                    .startDefaultRequest(config.getServer())
                    .addHeader("sso_header", JSON.toJSONString(ssoRequestHeader))
                    .doPost(StringSimpleHttp.buildJsonEntity(request));
        } catch (Exception e) {
            System.err.println(e.toString());
            response = e.getMessage();
            throw e;
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