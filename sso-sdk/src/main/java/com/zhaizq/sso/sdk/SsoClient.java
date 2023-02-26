package com.zhaizq.sso.sdk;

import com.alibaba.fastjson.JSON;
import com.zhaizq.sso.sdk.domain.request.SsoRequest;
import com.zhaizq.sso.sdk.domain.response.SsoResponse;

import java.util.UUID;

public class SsoClient {
    private String url = "http://localhost/api";
    private String app;
    private String privateKey;

    public SsoResponse request(Object a, Object b) {
        return null;
    }

    public SsoResponse doRequest(String method, SsoRequest ssoRequest) {
        try {
            ssoRequest = ssoRequest == null ? new SsoRequest() : ssoRequest;
            ssoRequest.set_app(app);
            ssoRequest.set_uuid(UUID.randomUUID().toString());
            ssoRequest.set_method(method);
            ssoRequest.set_timestamp(System.currentTimeMillis());

            String body = JSON.toJSONString(ssoRequest);
            String sign = StringRsaUtil.sign(body, privateKey);
            String url = String.format("%s?sign=%s", this.url, sign);

            long time = System.currentTimeMillis();
            System.out.printf("[APP -> SSO] url: %s, body: %s%n", url, body);
            String result = HttpUtil.postJson(url, body);
            System.out.printf("[APP <- SSO] result: %s, time: %d(ms)%n", result, System.currentTimeMillis() - time);
            return JSON.parseObject(result, SsoResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}