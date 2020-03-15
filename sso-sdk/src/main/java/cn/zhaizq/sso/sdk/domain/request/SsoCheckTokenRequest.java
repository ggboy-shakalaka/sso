package cn.zhaizq.sso.sdk.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SsoCheckTokenRequest extends SsoBaseRequest {
    private String token;

    public SsoCheckTokenRequest(String token) {
        this.token = token;
    }
}