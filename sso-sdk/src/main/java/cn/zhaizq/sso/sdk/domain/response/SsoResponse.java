package cn.zhaizq.sso.sdk.domain.response;

public class SsoResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public SsoResponse<T> code(Integer code) {
        this.code = code;
        return this;
    }

    public Integer code() {
        return this.code;
    }

    public SsoResponse<T> message(String message) {
        this.message = message;
        return this;
    }

    public String message() {
        return this.message;
    }

    public SsoResponse<T> data(T data) {
        this.data = data;
        return this;
    }

    public T data() {
        return this.data;
    }
}