package cn.zhaizq.sso.sdk;

import cn.zhaizq.sso.sdk.domain.SsoUser;
import cn.zhaizq.sso.sdk.domain.request.SsoLoginRequest;
import cn.zhaizq.sso.sdk.domain.response.SsoResponse;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URI;

public class SsoService {
    public SsoApi ssoApi;

    public SsoService(String server, String appId) {
        this.ssoApi = new SsoApi(server, appId);
    }

    public String getRefreshTokenPath(String redirect, String login_url) {
        String path = ssoApi.getSsoConfig().getServerPath() + ssoApi.getSsoConfig().getRefreshTokenPath();
        URIBuilder uri = new URIBuilder(URI.create(path));
        uri.addParameter(SsoConstant.REDIRECT, redirect);
        uri.addParameter(SsoConstant.LOGIN_URL, login_url);
        return uri.toString();
    }

    public SsoResponse<SsoUser> checkToken(String token) throws IOException {
        return ssoApi.checkToken(token);
    }

    public SsoResponse<String>  login(String name, String pwd) throws IOException {
        SsoLoginRequest req = new SsoLoginRequest();
        req.setName(name);
        req.setPassword(pwd);
        return ssoApi.login(req);
    }

    public SsoResponse<String>  logout(String token) throws IOException {
        return ssoApi.logout(token);
    }
}