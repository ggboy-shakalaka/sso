package com.zhaizq.sso.common.enums;

import com.alibaba.fastjson2.JSON;
import com.zhaizq.sso.common.exception.BusinessException;

public class RequestJsonFormat implements RequestFormat {
    public <T> T parse(String data, Class<T> clazz) {
        try {
            return JSON.parseObject(data, clazz);
        } catch (Exception e) {
            throw new BusinessException("请求字段解析失败");
        }
    }

    public String format(Object object) {
        return JSON.toJSONString(object);
    }
}