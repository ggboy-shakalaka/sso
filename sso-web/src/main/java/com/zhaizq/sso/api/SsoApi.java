package com.zhaizq.sso.api;

import com.zhaizq.sso.common.enums.ApiMethod;
import com.zhaizq.sso.common.exception.BusinessException;

@ApiMethod
public class SsoApi {
    @ApiMethod("health")
    public String health() {
        return "OK";
    }

    @ApiMethod("check_token")
    public Object checkToken(String token) {
        if (!"123456".equals(token))
            throw new BusinessException("Token失效");

        return "zhangsan";
    }
}