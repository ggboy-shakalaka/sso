package cn.zhaizq.sso.sdk.domain.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SsoLoginRequest extends SsoBaseRequest {
    private String name;
    private String password;
    private String code;
}