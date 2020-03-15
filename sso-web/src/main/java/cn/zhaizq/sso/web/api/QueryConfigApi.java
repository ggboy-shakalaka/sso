package cn.zhaizq.sso.web.api;

import cn.zhaizq.sso.sdk.SsoConstant;
import cn.zhaizq.sso.sdk.domain.SsoConfig;
import cn.zhaizq.sso.sdk.domain.SsoUser;
import cn.zhaizq.sso.sdk.domain.request.SsoCheckTokenRequest;
import cn.zhaizq.sso.sdk.domain.response.SsoResponse;
import cn.zhaizq.sso.service.domain.entry.User;
import cn.zhaizq.sso.service.service.LoginService;
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