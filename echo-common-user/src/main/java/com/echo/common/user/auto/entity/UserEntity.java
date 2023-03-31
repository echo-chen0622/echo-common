package com.echo.common.user.auto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.echo.common.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author ： Echo
 * @date ： 2022-7-26
 * @version : 1.0
 */

/**
 * 用户表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "e_user")
public class UserEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 自增id，不赋予业务意义
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
    /**
     * 登录名，用以用户登录
     */
    @TableField(value = "login_name")
    private String loginName;
    /**
     * 密码，默认为123456
     */
    @TableField(value = "`password`")
    private String password;
    /**
     * 用户真实姓名，默认为空
     */
    @TableField(value = "real_name")
    private String realName;
    /**
     * 用户手机号，默认为空
     */
    @TableField(value = "mobile")
    private Integer mobile;
    /**
     * 用户权限code，直接存数组，未来有真实用户体系再去掉，默认为空
     */
    @TableField(value = "permissions")
    private String permissions;

}
