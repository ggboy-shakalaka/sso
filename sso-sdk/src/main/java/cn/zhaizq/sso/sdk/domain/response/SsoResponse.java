package cn.zhaizq.sso.sdk.domain.response;

import com.alibaba.fastjson.JSON;
import lombok.Getter;

@Getter
public class SsoResponse {
    private Integer code;
    private String message;
    private String data;

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
}