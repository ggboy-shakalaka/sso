package cn.zhaizq.sso.sdk.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SsoCheckTokenRequest {
    private String token;

    public static SsoCheckTokenRequest build(String token) {
        SsoCheckTokenRequest ssoCheckTokenRequest = new SsoCheckTokenRequest();
        ssoCheckTokenRequest.setToken(token);
        return ssoCheckTokenRequest;
    }
}