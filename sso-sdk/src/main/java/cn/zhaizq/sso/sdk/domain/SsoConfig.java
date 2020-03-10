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
    private String server;
    private String appId;
    private String loginUrl;
    private String[] ignore = {};

    private ServerConfig serverConfig;

    public String getQueryConfigUrl() {
        return server + "/api/appId/query_config";
    }

    public String getCheckTokenUrl() {
        return server + serverConfig.getCheckTokenPath();
    }

    public String getServerLoginUrl() {
        return server + serverConfig.getLoginPath();
    }

    public String getServerLogoutUrl() {
        return server + serverConfig.getLogoutPath();
    }

    public String getRefreshTokenUrl(String redirect) {
        String url = serverConfig.getServerUrl() + serverConfig.getRefreshTokenPath();

        URIBuilder builder = new URIBuilder(URI.create(url));
        builder.addParameter(SsoConstant.LOGIN_URL, loginUrl);
        builder.addParameter(SsoConstant.REDIRECT, redirect);

        return builder.toString();
    }

    @Getter
    @Setter
    public final static class ServerConfig {
        private String serverUrl;
        private String loginPath;
        private String logoutPath;
        private String refreshTokenPath;
        private String checkTokenPath;
    }
}