package com.zhaizq.sso.common.enums;

import com.zhaizq.sso.common.exception.BusinessException;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum RequestFormat {
    XML("application/xml") {
        public <T> T parse(String data, Class<T> clazz) {
            throw new BusinessException("请求格式暂不支持");
        }

        public String format(Object object) {
            throw new BusinessException("请求格式暂不支持");
        }
    },
    JSON("application/json") {
        public <T> T parse(String data, Class<T> clazz) {
            try {
                return com.alibaba.fastjson.JSON.parseObject(data, clazz);
            } catch (Exception e) {
                throw new BusinessException("请求字段解析失败");
            }
        }

        public String format(Object object) {
            return com.alibaba.fastjson.JSON.toJSONString(object);
        }
    }
    ;

    private final String mimeType;

    public static RequestFormat getByType(String mimeType) {
        return mimeType == null ? null : Arrays.stream(RequestFormat.values()).filter(v -> mimeType.startsWith(v.mimeType)).findAny().orElse(null);
    }

    public abstract <T> T parse(String data, Class<T> clazz);
    public abstract String format(Object object);
}