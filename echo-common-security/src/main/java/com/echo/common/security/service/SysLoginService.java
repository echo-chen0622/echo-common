package com.echo.common.security.service;

import cn.hutool.core.text.CharSequenceUtil;
import com.echo.common.cache.config.MyCache;
import com.echo.common.core.constant.UserConstants;
import com.echo.common.security.context.AuthenticationContextHolder;
import com.echo.common.security.entity.LoginUser;
import com.echo.common.security.entity.SysUser;
import com.echo.common.security.utils.UserUtils;
import com.echo.common.web.exception.ApiException;
import com.echo.common.web.response.ResultCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 登录校验方法
 *
 * @author Echo
 */
@Component
public class SysLoginService {
    @Resource
    private UserUtils userUtils;

    @Resource
    private MyCache myCache;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private SysUserService userService;

    @Resource
    private SysConfigService configService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        // 验证码校验
        validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            throw new ApiException(ResultCode.LOGIN_ERROR);
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 登录缓存，并返回token
        return userUtils.login(loginUser).getToken();
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = "CAPTCHA_CODE_KEY::" + uuid;
            String captcha = myCache.getCache(verifyKey);
            myCache.delCache(verifyKey);
            if (captcha == null) {
                throw new ApiException(ResultCode.CAPTCHA_EXPIRED);
            }
            if (!code.equalsIgnoreCase(captcha)) {
                throw new ApiException(ResultCode.CAPTCHA_EXPIRED);
            }
        }
    }

    /**
     * 登录前置校验
     *
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password) {
        // 用户名或密码为空 错误
        if (CharSequenceUtil.isEmpty(username) || CharSequenceUtil.isEmpty(password)) {
            throw new ApiException(ResultCode.LOGIN_ERROR);
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            throw new ApiException(ResultCode.LOGIN_ERROR);
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            throw new ApiException(ResultCode.LOGIN_ERROR);
        }
//        // IP黑名单校验
//        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
//        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
//            throw new BlackListException();
//        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp("127.0.0.1");
        sysUser.setLoginDate(new Date());
        userService.updateUserProfile(sysUser);
    }
}
