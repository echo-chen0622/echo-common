package com.echo.common.security.utils;

import com.echo.common.security.entity.LoginUser;
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
    public LoginUser getUserFromCache(String token) {
        //如果从缓存里拿不到，就直接返回null
        return null;
    }

    @CachePut(value = USER_SESSION_KEY, key = "#token", cacheManager = "userCacheManager")
    public LoginUser login(String token, LoginUser user) {
        //如果从缓存里拿不到，就直接返回null
        user.setToken(token);
        return user;
    }

    @CacheEvict(value = USER_SESSION_KEY, key = "#token", cacheManager = "userCacheManager")
    public void logout(String token) {
        log.info("delete user :{}", token);
    }

    public void refreshToken(String token, LoginUser loginUser) {
        logout(token);
        login(token, loginUser);
    }
    //endregion
}
