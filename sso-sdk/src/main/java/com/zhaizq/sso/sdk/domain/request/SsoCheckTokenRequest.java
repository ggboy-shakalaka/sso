package com.zhaizq.sso.sdk.domain.request;

import lombok.Data;

@Data
public class SsoCheckTokenRequest extends SsoBaseRequest {
    private String token;
}