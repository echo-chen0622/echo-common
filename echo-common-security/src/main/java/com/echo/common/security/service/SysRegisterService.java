package com.echo.common.security.service;

import cn.hutool.core.text.CharSequenceUtil;
import com.echo.common.cache.config.MyCache;
import com.echo.common.core.constant.UserConstants;
import com.echo.common.security.entity.RegisterBody;
import com.echo.common.security.entity.SysUser;
import com.echo.common.security.utils.SecurityUtils;
import com.echo.common.web.exception.ApiException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 注册校验方法
 *
 * @author Echo
 */
@Component
public class SysRegisterService {
    @Resource
    private SysUserService userService;

    @Resource
    private SysConfigService configService;

    @Resource
    private MyCache myCache;

    /**
     * 注册
     */
    public String register(RegisterBody registerBody) {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);

        // 验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            validateCaptcha(registerBody.getCode(), registerBody.getUuid());
        }

        if (CharSequenceUtil.isEmpty(username)) {
            msg = "用户名不能为空";
        } else if (CharSequenceUtil.isEmpty(password)) {
            msg = "用户密码不能为空";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else if (!userService.checkUserNameUnique(sysUser)) {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        } else {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
            }
        }
        return msg;
    }

    /**
     * 校验验证码
     *
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String code, String uuid) {
        String verifyKey = "CAPTCHA_CODE_KEY::" + uuid;
        String captcha = myCache.getCache(verifyKey);
        myCache.delCache(verifyKey);
        if (captcha == null) {
            throw new ApiException("验证码已失效");
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new ApiException("验证码不正确");
        }
    }
}
