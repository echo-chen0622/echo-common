package com.echo.common.user.dto;

import com.echo.common.user.auto.entity.MenuEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单管理 -- 临时的dto，做简单的菜单权限管理
 *
 * @author echo
 * @date 2016年9月18日 上午9:26:39
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysMenuDto extends MenuEntity implements Serializable {
    public static final String SYS_MENU_ROOT = "0"; // 根菜单
    public static final String SYS_MENU_TYPE_CATELOG = "0"; // 目录
    public static final String SYS_MENU_TYPE_MENU = "1"; // 目录
    public static final String SYS_MENU_TYPE_BUTTON = "2"; // 按钮
    public static final String DEFAULT_ROOT_ID = "0"; // 默认父级根节点Id
    public static final String NODE_STATUS_NORMAL = "0"; // 正常状态
    public static final String NODE_STATUS_DISABLE = "1"; // 禁用状态

    /**
     * 父级Id
     */
    private String parentId = "0";
    /**
     * 父级名称
     */
    private String parentName = "";
    /**
     * 所有父级编号
     */
    private String parentIds = "[0]";
    /**
     * 是否含有子集节点 0：不含有 1：含有
     */
    private Integer hasChildren = 0;
    /**
     * 节点状态0：正常 1:禁用
     */
    private Integer status = 0;
    /**
     * 父级
     */
    private SysMenuDto parent = null;
    /**
     * 包含子集
     */
    private List<SysMenuDto> childList = null;
    /**
     * 菜单类型 0：目录 1：菜单 2：按钮
     */
    private Integer menuType = 1;
    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String perms;

    public String getPerms() {
        return getPermission();
    }
}
