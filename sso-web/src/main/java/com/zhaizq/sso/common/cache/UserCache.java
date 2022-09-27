package com.zhaizq.sso.common.cache;

import com.zhaizq.sso.mapper.entry.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserCache {
    private Map<String, User> cache = new HashMap<>();

    public User get(String token) {
        return token == null ? null : cache.get(token);
    }

    public User put(String token, User user) {
        return cache.put(token, user);
    }
}