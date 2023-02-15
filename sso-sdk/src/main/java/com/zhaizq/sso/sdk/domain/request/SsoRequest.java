package com.zhaizq.sso.sdk.domain.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SsoRequest {
    @NotEmpty
    private String _uuid;
    @NotEmpty
    private String _app;
    @NotEmpty
    private String _method;
    @NotNull
    private Long _timestamp;
}