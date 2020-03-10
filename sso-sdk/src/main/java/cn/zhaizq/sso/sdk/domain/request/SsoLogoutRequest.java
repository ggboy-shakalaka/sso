package cn.zhaizq.sso.sdk.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SsoLogoutRequest {
    private String token;

    public static SsoLogoutRequest build(String token) {
        SsoLogoutRequest ssoLogoutRequest = new SsoLogoutRequest();
        ssoLogoutRequest.setToken(token);
        return ssoLogoutRequest;
    }
}