package com.echo.common.security.service;

import com.echo.common.cache.config.MyCache;
import com.echo.common.security.context.AuthenticationContextHolder;
import com.echo.common.security.entity.SysUser;
import com.echo.common.security.utils.SecurityUtils;
import com.echo.common.web.exception.ApiException;
import com.echo.common.web.response.ResultCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录密码方法
 *
 * @author Echo
 */
@Component
public class SysPasswordService {
    @Resource
    private MyCache myCache;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username) {
        return "PWD_ERR_CNT_KEY::" + username;
    }

    public void validate(SysUser user) {
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        String cache = myCache.getCache(getCacheKey(username));
        Integer retryCount;
        if (cache != null) {
            retryCount =Integer.valueOf(cache);
        }else {
            retryCount = 0;
        }

        if (retryCount >= maxRetryCount) {
            throw new ApiException(ResultCode.LOGIN_ERROR, maxRetryCount, lockTime);
        }

        if (!matches(user, password)) {
            retryCount = retryCount + 1;
            myCache.saveCache(getCacheKey(username), retryCount.toString());
            throw new ApiException(ResultCode.LOGIN_ERROR, maxRetryCount - retryCount);
        } else {
            clearLoginRecordCache(username);
        }
    }

    public boolean matches(SysUser user, String rawPassword) {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName) {
        myCache.delCache(getCacheKey(loginName));
    }
}
