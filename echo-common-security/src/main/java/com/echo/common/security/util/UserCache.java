package com.echo.common.security.util;

import com.echo.common.security.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserCache {
    public static final String USER_SESSION_KEY = "echo_user_session_";

    //region 缓存用户 这里只作为临时用法，只用本地缓存做
    @Cacheable(value = USER_SESSION_KEY, key = "#token", cacheManager = "userCacheManager")
    public UserDto getUserFromCache(String token) {
        //如果从缓存里拿不到，就直接返回null
        return null;
    }

    @CachePut(value = USER_SESSION_KEY, key = "#token", cacheManager = "userCacheManager")
    public UserDto login(String token, UserDto user) {
        //如果从缓存里拿不到，就直接返回null
        user.setToken(token);
        return user;
    }

    @CacheEvict(value = USER_SESSION_KEY, key = "#token", cacheManager = "userCacheManager")
    public void logout(String token) {
        log.info("delete user :{}", token);
    }
    //endregion
}
