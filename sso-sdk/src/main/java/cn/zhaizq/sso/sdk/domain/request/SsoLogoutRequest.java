package cn.zhaizq.sso.sdk.domain.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SsoLogoutRequest extends SsoBaseRequest {
    private String token;
}