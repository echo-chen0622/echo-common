package com.echo.common.user.controller;

import com.echo.common.security.dto.UserDto;
import com.echo.common.security.util.UserUtils;
import com.echo.common.web.exception.ApiException;
import com.echo.common.web.response.BaseResponse;
import com.echo.common.web.response.ResultCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 *
 * @author ： Echo
 * @version : 1.0
 * @date ： 2022-7-22
 */
@RestController
@RequestMapping("/sys/user")
public class UserController {

    @Resource
    private UserUtils userUtils;

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public BaseResponse<UserDto> info() {
        UserDto userDto = userUtils.getUser();
        if (userDto == null) {
            throw new ApiException(ResultCode.LOGIN_ERROR);
        }
        return new BaseResponse<>(userDto);
    }
}
