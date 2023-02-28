package com.zhaizq.sso.controller.api;

import com.zhaizq.sso.sdk.domain.SsoRequest;

import java.lang.reflect.ParameterizedType;

public abstract class BaseApi2<T extends SsoRequest> {
    abstract Object service(T data) throws Exception;

    @SuppressWarnings("unchecked")
    public Class<T> getParamClass() {
        return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}