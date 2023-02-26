package com.zhaizq.sso.common.cache;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CacheMap<K, V> {
    private final Map<K, Cache<V>> cacheMap = new ConcurrentHashMap<>();

    public CacheMap() {
        Runnable runnable = this::cleanup;

        new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), v -> {
            Thread thread = new Thread(v, "cache-map-cleaner");
            thread.setDaemon(true);
            thread.setPriority(Thread.MIN_PRIORITY);
            return thread;
        }).scheduleAtFixedRate(runnable, 0, 30, TimeUnit.SECONDS);
    }

    public void put(K k, V v, long expire) {
        cacheMap.put(k, new Cache<>(v, System.currentTimeMillis() + expire));
    }

    public V get(K k) {
        Cache<V> cache = cacheMap.get(k);
        return cache != null && cache.expire > System.currentTimeMillis() ? cache.value : null;
    }

    private void cleanup() {
        long limit = System.currentTimeMillis();
        cacheMap.entrySet().stream().filter(v -> v.getValue().expire < limit).forEach(v -> {
            cacheMap.computeIfPresent(v.getKey(), (key, cache) -> cache.expire < limit ? null : cache);
        });
    }

    @AllArgsConstructor
    private static class Cache<V> {
        private final V value;
        private long expire;
    }
}