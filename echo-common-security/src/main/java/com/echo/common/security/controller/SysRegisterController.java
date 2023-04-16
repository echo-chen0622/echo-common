package com.echo.common.security.controller;

import cn.hutool.core.text.CharSequenceUtil;
import com.echo.common.security.entity.RegisterBody;
import com.echo.common.security.service.SysConfigService;
import com.echo.common.security.service.SysRegisterService;
import com.echo.common.web.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册验证
 *
 * @author echo
 */
@RestController
public class SysRegisterController extends BaseController {
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private SysConfigService configService;

    @PostMapping("/register")
    BaseResponse<Object> register(@RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return BaseResponse.error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return CharSequenceUtil.isEmpty(msg) ? BaseResponse.ok() : BaseResponse.error(msg);
    }
}
