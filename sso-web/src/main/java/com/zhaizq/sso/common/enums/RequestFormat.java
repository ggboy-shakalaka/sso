package com.zhaizq.sso.common.enums;

public interface RequestFormat {
    <T> T parse(String data, Class<T> clazz);
    String format(Object object);
}