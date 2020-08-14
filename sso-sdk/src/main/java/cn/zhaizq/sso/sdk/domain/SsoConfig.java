package cn.zhaizq.sso.sdk.domain;

import cn.zhaizq.sso.sdk.SsoConstant;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;

@Getter
@Setter
public class SsoConfig {
    private boolean local = false;
    private String logger;
    private String server;
    private String appId;
    private String loginUrl;
    private String privateKey;
    private String[] ignore = {};

    private ServerConfig serverConfig;

    public String getRefreshTokenUrl(String redirect) {
        String url = serverConfig.getServerUrl() + serverConfig.getRefreshTokenPath();

        URIBuilder builder = new URIBuilder(URI.create(url));
        builder.addParameter(SsoConstant.LOGIN_URL, loginUrl);
        builder.addParameter(SsoConstant.REDIRECT, redirect);
        builder.addParameter(SsoConstant.APP_ID, appId);

        return builder.toString();
    }

    @Getter
    @Setter
    public final static class ServerConfig {
        private String serverUrl;
        private String refreshTokenPath;
    }
}