package com.echo.common.security.dto;

import lombok.Data;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author ： Echo
 * @version : 1.0
 * @date ： 2022-7-21
 */
@Data
public class UserDto {

    /**
     * .
     * 用户登录名
     */
    private String loginName;

    /**
     * 用户名称（可以是姓名或者昵称
     */
    private String realName;

    /**
     * token
     */
    private String token;

    /**
     * 用户手机号
     */
    private Integer mobile;

    /**
     * 用户角色集合
     */
    private Set<String> roles;

    /**
     * 用户权限集合
     */
    private Set<String> permissions;
}
