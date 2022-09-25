package com.zhaizq.sso.sdk.domain.request;

import lombok.Data;

@Data
public class SsoQueryLoginPublicKeyRequest extends SsoBaseRequest {
    private String name;
}