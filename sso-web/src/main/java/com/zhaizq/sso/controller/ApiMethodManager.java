package com.zhaizq.sso.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.zhaizq.sso.common.enums.ApiMethod;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class ApiMethodManager {
    private Map<String, ApiMethodDefinition> apiMethodMap = new ConcurrentHashMap<>();
    private LocalVariableTableParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    public ApiMethodManager(ApplicationContext applicationContext) {
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(ApiMethod.class);
        for (Object bean : beanMap.values()) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                ApiMethod apiMethod = method.getAnnotation(ApiMethod.class);
                if (apiMethod == null) continue;

                // do registration
                this.apiMethodMap.compute(apiMethod.value(), (key, value) -> {
                    if (value != null) throw new RuntimeException("duplicate key error -> ApiMethod[" + value + "]");
                    return new ApiMethodDefinition(key, bean, method, method.getParameters(), PARAMETER_NAME_DISCOVERER.getParameterNames(method));
                });
            }
        }
    }

    public boolean exists(String method) {
        return method != null && this.apiMethodMap.containsKey(method);
    }

    public Object invoke(String method, String body) {
        return this.apiMethodMap.get(method).invoke(body);
    }

    @Data
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ApiMethodDefinition {
        private String name;
        private Object bean;
        private Method method;
        private Parameter[] parameters;
        private String[] parameterNames;

        private Object[] convertArgs(String str) throws InvocationTargetException, IllegalAccessException {
            Object[] args = new Object[parameters.length];
            JSONObject jsonObject = JSON.parseObject(str);
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                String parameterName = parameterNames[i];
                if (jsonObject.containsKey(parameterName)) {
                    args[i] = jsonObject.getObject(parameterName, parameter.getType());
                } else {
                    args[i] = jsonObject.to(parameter.getType());
                }
            }

            return args;
        }

        private Object invoke(String body) {
            try {
                Object[] args = this.convertArgs(body);
                return method.invoke(bean, args);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}