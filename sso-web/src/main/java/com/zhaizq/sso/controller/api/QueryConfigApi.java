package com.zhaizq.sso.controller.api;

import com.zhaizq.sso.sdk.SsoConstant;
import com.zhaizq.sso.sdk.domain.SsoConfig;
import com.zhaizq.sso.sdk.domain.request.SsoCheckTokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(SsoConstant.Method.QUERY_CONFIG)
public class QueryConfigApi extends BaseApi<SsoCheckTokenRequest> {
    @Autowired
    private SsoConfig ssoConfig;

    @Override
    Object doService(SsoCheckTokenRequest data) {
        SsoConfig.ServerConfig config = new SsoConfig.ServerConfig();
        config.setServerUrl(ssoConfig.getServerConfig().getServerUrl());
        config.setRefreshTokenPath("/api/login/refresh_token");
        return config;
    }
}