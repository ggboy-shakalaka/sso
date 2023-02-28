package com.zhaizq.sso.sdk.domain;

import lombok.Data;

@Data
public class SsoConfig {
    private String server = "http://localhost:8080/api";
    private String host = "http://localhost:8081";

    private String app = "demo";
    private String privateKey = "";

    private String[] ignore = {"/uncheck", "/api"};
}