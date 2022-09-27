package com.zhaizq.sso.sdk.domain.response;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class SsoResponse {
    private Integer code;
    private String message;
    private Object data;

    public static SsoResponse success(Object data) {
        return success(null, data);
    }

    public static SsoResponse success(String msg, Object data) {
        return create(200, msg, data);
    }

    public static SsoResponse create(Integer code, String msg, Object data) {
        return new SsoResponse(code, msg, data);
    }
}