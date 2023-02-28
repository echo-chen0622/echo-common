package com.echo.common.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * @author Echo
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 配置缓存管理器
     *
     * @return 缓存管理器
     */
    @Bean("userCacheManager")
    @Primary
    public CacheManager userCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                                         // 设置最后一次写入或访问后经过固定时间过期 -- 一天过期
                                         .expireAfterAccess(1, TimeUnit.DAYS)
                                         // 初始的缓存空间大小
                                         .initialCapacity(100)
                                         // 缓存的最大条数
                                         .maximumSize(1000));
        return cacheManager;
    }

    /**
     * 配置缓存管理器
     *
     * @return 缓存管理器
     */
    @Bean("defaultCacheManager")
    public CacheManager defaultCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                                         // 设置最后一次写入或访问后经过固定时间过期 -- 十分钟过期
                                         .expireAfterAccess(10, TimeUnit.MINUTES)
                                         // 初始的缓存空间大小
                                         .initialCapacity(100)
                                         // 缓存的最大条数
                                         .maximumSize(1000));
        return cacheManager;
    }

}
