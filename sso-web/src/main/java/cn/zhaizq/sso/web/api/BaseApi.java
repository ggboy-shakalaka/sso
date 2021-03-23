package cn.zhaizq.sso.web.api;

import cn.zhaizq.sso.common.exception.BusinessException;
import cn.zhaizq.sso.sdk.domain.request.SsoBaseRequest;
import cn.zhaizq.sso.web.utils.ValidateUtil;

import java.lang.reflect.ParameterizedType;

public abstract class BaseApi<T extends SsoBaseRequest> {
    @SuppressWarnings("unchecked")
    public Object service(SsoBaseRequest data) throws Exception {
        verify((T) data);
        return doService((T) data);
    }

    void verify(T data) throws BusinessException {
        ValidateUtil.validate(data);
    }

    abstract Object doService(T data) throws Exception;

    @SuppressWarnings("unchecked")
    public Class<T> getParamClass() {
        return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}