package com.zhaizq.sso.sdk.domain;

import lombok.Data;

@Data
public class SsoServerConfig {
    private boolean init;
    private String refreshTokenUrl = "http://localhost:8080/uncheck/refresh-token";
}