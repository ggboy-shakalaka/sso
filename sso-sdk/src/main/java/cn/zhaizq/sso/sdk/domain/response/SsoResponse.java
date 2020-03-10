package cn.zhaizq.sso.sdk.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SsoResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public SsoResponse<T> code(Integer code) {
        this.code = code;
        return this;
    }

    public SsoResponse<T> message(String message) {
        this.message = message;
        return this;
    }

    public SsoResponse<T> data(T data) {
        this.data = data;
        return this;
    }
}