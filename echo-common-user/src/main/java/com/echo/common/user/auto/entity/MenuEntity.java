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
 * @date ： 2022-7-30
 * @version : 1.0
 */

/**
 * 关键字表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "e_menu")
public class MenuEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 自增id，不赋予业务意义
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
    /**
     * 权限code
     */
    @TableField(value = "permission")
    private String permission;
    /**
     * 排序号
     */
    @TableField(value = "sort_no")
    private Integer sortNo;
    /**
     * 菜单名称
     */
    @TableField(value = "`name`")
    private String name;
    /**
     * 路由
     */
    @TableField(value = "url")
    private String url;
    /**
     * icon
     */
    @TableField(value = "icon")
    private String icon;
}
