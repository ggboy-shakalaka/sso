package cn.zhaizq.sso.web.api;

import cn.zhaizq.sso.web.utils.ValidateUtil;
import com.ggboy.framework.common.exception.BusinessException;

import java.lang.reflect.ParameterizedType;

public abstract class BaseApi<T extends BaseApiParam> {
    @SuppressWarnings("unchecked")
    public Object service(BaseApiParam data) {
        verify((T) data);
        return doService((T) data);
    }

    void verify(T data) throws BusinessException {
        ValidateUtil.validate(data);
    }

    abstract Object doService(T data);

    @SuppressWarnings("unchecked")
    public Class<T> getParamClass() {
        return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}