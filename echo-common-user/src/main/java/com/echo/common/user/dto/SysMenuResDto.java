package com.echo.common.user.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 菜单管理 -- 临时的dto，做简单的菜单权限管理
 *
 * @author echo
 * @date 2016年9月18日 上午9:26:39
 */
@Data
public class SysMenuResDto implements Serializable {

    /**
     * 菜单
     */
    private List<SysMenuDto> menuList;

    /**
     * 权限
     */
    private Set<String> permissions;
}
