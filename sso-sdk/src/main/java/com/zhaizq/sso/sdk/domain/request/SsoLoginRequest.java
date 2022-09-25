package com.zhaizq.sso.sdk.domain.request;

import lombok.Data;

@Data
public class SsoLoginRequest extends SsoBaseRequest {
    private String name;
    private String password;
    private String code;
}