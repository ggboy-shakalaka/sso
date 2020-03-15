package cn.zhaizq.sso.sdk.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SsoQueryLoginPublicKeyRequest extends SsoBaseRequest {
    private String name;

    public SsoQueryLoginPublicKeyRequest(String name) {
        this.name = name;
    }
}