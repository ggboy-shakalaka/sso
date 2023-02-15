package com.zhaizq.sso.common.enums;

import com.zhaizq.sso.common.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;

public class RequestFormatFactory {
    private final static Map<String, RequestFormat> formatter = new HashMap<>();

    static {
        formatter.put("application/json", new RequestJsonFormat());
    }

    public static RequestFormat find(String type) {
        RequestFormat format = formatter.get(type);
        if (format == null)
            throw new BusinessException("请求格式[" + type + "]不支持");
        return formatter.get(type);
    }
}