package com.zhaizq.sso.sdk.domain.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class SsoBaseRequest {
    @Valid
    @NotNull
    private SsoRequestHeader header;
}