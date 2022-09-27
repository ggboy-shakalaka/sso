package com.zhaizq.sso.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;

@Component
public class ApiMethodHandler implements BeanPostProcessor {
    private final static ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    private final Map<String, MethodAccessor> methodAccessorMap = new HashMap<>();

    public MethodAccessor getMethodAccessor(String method) {
        return methodAccessorMap.get(method);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        for (Method method : targetClass.getDeclaredMethods()) {
            ApiMethod apiMethod = method.getDeclaredAnnotation(ApiMethod.class);
            if (apiMethod != null) {
                methodAccessorMap.put(apiMethod.value(), new MethodAccessor(bean, method));
            }
        }

        return bean;
    }

    @RequiredArgsConstructor
    public static class MethodAccessor {
        private final Object bean;
        private final Method method;
        private List<Map.Entry<String, Class<?>>> parameters;

        public Object invoke(BiFunction<String, Class<?>, Object> argument) throws InvocationTargetException, IllegalAccessException {
            Object[] objects = this.getParameters().stream().map(v -> argument.apply(v.getKey(), v.getValue())).toArray();
            return method.invoke(bean, objects);
        }

        public List<Map.Entry<String, Class<?>>> getParameters() {
            if (this.parameters == null) {
                List<Map.Entry<String, Class<?>>> parameters = new LinkedList<>();
                String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
                Class<?>[] parameterTypes = method.getParameterTypes();

                assert parameterNames != null;
                for (int i = 0; i < parameterTypes.length; i++) {
                    parameters.add(new AbstractMap.SimpleEntry<>(parameterNames[i], parameterTypes[i]));
                }

                this.parameters = parameters;
            }

            return this.parameters;
        }
    }

    public static BiFunction<String, Class<?>, Object> fastjsonArgument(String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        return jsonObject == null ? (k, v) -> null : jsonObject::getObject;
    }
}