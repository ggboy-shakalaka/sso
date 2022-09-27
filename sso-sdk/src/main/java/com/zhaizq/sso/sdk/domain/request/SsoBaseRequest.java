package com.zhaizq.sso.sdk.domain.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SsoBaseRequest {
    @NotEmpty
    private String uuid;
    @NotEmpty
    private String appId;
    @NotEmpty
    private String method;
    @NotNull
    private Long timestamp;
}