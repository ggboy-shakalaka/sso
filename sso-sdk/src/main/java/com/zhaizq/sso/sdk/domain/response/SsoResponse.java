package com.zhaizq.sso.sdk.domain.response;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class SsoResponse {
    private Integer code;
    private Object data;
    private String message;

    public SsoResponse code(Integer code) {
        this.code = code;
        return this;
    }

    public SsoResponse message(String message) {
        this.message = message;
        return this;
    }

    public SsoResponse data(Object data) {
        this.data = JSON.toJSONString(data);
        return this;
    }

    public static SsoResponse build(Integer code, Object data, String message) {
        SsoResponse ssoResponse = new SsoResponse();
        ssoResponse.setCode(code);
        ssoResponse.setData(data);
        ssoResponse.setMessage(message);
        return ssoResponse;
    }
}