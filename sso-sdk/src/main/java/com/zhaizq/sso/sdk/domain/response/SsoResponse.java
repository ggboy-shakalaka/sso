package com.zhaizq.sso.sdk.domain.response;

import lombok.Data;

@Data
public class SsoResponse {
    private Integer code;
    private Object data;
    private String message;

    public static SsoResponse build(Integer code, Object data, String message) {
        SsoResponse ssoResponse = new SsoResponse();
        ssoResponse.setCode(code);
        ssoResponse.setData(data);
        ssoResponse.setMessage(message);
        return ssoResponse;
    }
}