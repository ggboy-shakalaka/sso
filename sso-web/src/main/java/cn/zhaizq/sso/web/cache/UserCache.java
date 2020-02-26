package cn.zhaizq.sso.web.cache;

import cn.zhaizq.sso.service.domain.entry.User;
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