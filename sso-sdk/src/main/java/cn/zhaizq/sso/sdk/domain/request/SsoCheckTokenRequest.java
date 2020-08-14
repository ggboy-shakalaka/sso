package cn.zhaizq.sso.sdk.domain.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SsoCheckTokenRequest extends SsoBaseRequest {
    private String token;
}