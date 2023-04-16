package com.echo.common.cache.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MyCache {
    public static final String USER_SESSION_KEY = "echo_cache_";

    //region 缓存 这里只作为临时用法，只用本地缓存做
    @Cacheable(value = USER_SESSION_KEY, key = "#key", cacheManager = "defaultCacheManager")
    public String getCache(String key) {
        //如果从缓存里拿不到，就直接返回null
        return null;
    }

    @CachePut(value = USER_SESSION_KEY, key = "#key", cacheManager = "defaultCacheManager")
    public String saveCache(String key, String cache) {
        //如果从缓存里拿不到，就直接返回null
        return cache;
    }

    @CacheEvict(value = USER_SESSION_KEY, key = "#key", cacheManager = "defaultCacheManager")
    public void delCache(String key) {
        log.info("delete cache :{}", key);
    }
    //endregion
}
