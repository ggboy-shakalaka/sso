package cn.zhaizq.sso.sdk.domain.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SsoQueryLoginPublicKeyRequest extends SsoBaseRequest {
    private String name;
}