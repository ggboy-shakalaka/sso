package com.zhaizq.sso.api;

import com.zhaizq.sso.common.enums.ApiMethod;

@ApiMethod
public class SsoApi {
    @ApiMethod("health")
    public String health() {
        return "OK";
    }
}