package com.zhaizq.sso.sdk.domain;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SsoRequest {
    @NotEmpty
    private String app;
    @NotEmpty
    private String uuid;
    @NotEmpty
    private String method;
    @NotNull
    private Long timestamp;
    @Valid
    private Object params;
}