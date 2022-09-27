package com.zhaizq.sso.common.exception;

import lombok.Getter;

public class BusinessException extends RuntimeException {
    @Getter private final Integer code;

    public BusinessException(String message) {
        this(400, message);
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}