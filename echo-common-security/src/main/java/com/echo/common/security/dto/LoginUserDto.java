package com.echo.common.security.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created with IntelliJ IDEA.
 *
 * @author ： Echo
 * @version : 1.0
 * @date ： 2022-7-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LoginUserDto extends UserDto {

    /**
     * 登录密码
     */
    private String password;

}
