package com.zhaizq.sso.sdk.domain.request;

import lombok.Data;

@Data
public class SsoLogoutRequest extends SsoBaseRequest {
    private String token;
}