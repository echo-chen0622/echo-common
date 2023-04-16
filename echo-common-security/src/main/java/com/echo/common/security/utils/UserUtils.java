package com.echo.common.security.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.CharSequenceUtil;
import com.echo.common.security.entity.LoginUser;
import com.echo.common.web.exception.ApiException;
import com.echo.common.web.response.ResultCode;
import com.echo.common.web.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class UserUtils {
    // 令牌有效期（默认两天）
    @Value("${token.expireTime:2880}")
    private int expireTime;

    public static final String ECHO_COM_TOKEN = "echo_com_token";
    public static final String UNDEFINED = "undefined";
    public static final String USER_SESSION_KEY = "echo_user_session_";
    public static final String TOKEN = "token";

    /**
     * 自动刷新时间 20 分钟
     */
    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    protected static final long MILLIS_MINUTE = 60 * 1000;

    @Resource
    private HttpServletRequest httpServletRequest;
    @Resource
    private UserCache userCache;

    /**
     * 检验token的值是否合法 (非空且不等于undefined )
     *
     * @param token token
     * @return 是否合法
     */
    private static boolean checkToken(String token) {
        return CharSequenceUtil.isNotEmpty(token) && !token.trim().equalsIgnoreCase(UNDEFINED);
    }

    /**
     * 获取当前用户
     *
     * @return 取不到返回 new User()
     */
    public LoginUser getUser() {
        LoginUser user;
        //尝试从请求中直接读取
        user = (LoginUser) httpServletRequest.getAttribute(USER_SESSION_KEY);
        if (user != null) {
            return user;
        }
        String token = getToken();
        if (token == null) {
            throw new ApiException(ResultCode.TOKEN_EMPTY);
        }
        user = userCache.getUserFromCache(token);
        httpServletRequest.setAttribute(USER_SESSION_KEY, user);
        //获取缓存
        return user;
    }

    @Nullable
    private String getToken() {
        String token = WebUtils.getCookieVal(httpServletRequest, ECHO_COM_TOKEN);
        if (CharSequenceUtil.isEmpty(token) || !checkToken(token)) {
            token = httpServletRequest.getHeader(TOKEN);
        }
        if (CharSequenceUtil.isEmpty(token)) {
            token = httpServletRequest.getParameter(TOKEN);
        }
        if (CharSequenceUtil.isEmpty(token)) {
            return null;
        }
        return token;
    }

    public LoginUser login(LoginUser user) {
        //如果从缓存里拿不到，就直接返回null
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        return userCache.login(token, user);
    }

    public void logout() {
        String token = getToken();
        userCache.logout(token);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        userCache.refreshToken(loginUser.getToken(), loginUser);
    }
}
