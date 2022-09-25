package com.zhaizq.sso.common.cache;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenCache {
    private final Map<String, String> cache = new HashMap<>();

    public String get(String name) {
        return cache.get(name);
    }

    public String put(String name, String token) {
        return cache.put(name, token);
    }
}