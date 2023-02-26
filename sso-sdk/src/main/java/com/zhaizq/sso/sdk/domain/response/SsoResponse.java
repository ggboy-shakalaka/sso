package com.zhaizq.sso.sdk.domain.response;

import lombok.Data;

@Data
public class SsoResponse<T> {
    private Integer code;
    private T data;
    private String message;

    public SsoResponse code(Integer code) {
        this.code = code;
        return this;
    }

    public SsoResponse message(String message) {
        this.message = message;
        return this;
    }

    public SsoResponse data(T data) {
        this.data = data;
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