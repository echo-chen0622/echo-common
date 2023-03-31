package com.echo.common.user.controller;

import com.echo.common.security.LoginRequired;
import com.echo.common.security.dto.LoginUserDto;
import com.echo.common.security.dto.UserDto;
import com.echo.common.security.util.UserUtils;
import com.echo.common.user.auto.entity.MenuEntity;
import com.echo.common.user.auto.service.MenuService;
import com.echo.common.user.auto.service.UserService;
import com.echo.common.user.dto.SysMenuDto;
import com.echo.common.user.dto.SysMenuResDto;
import com.echo.common.web.response.BaseResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author ： Echo
 * @version : 1.0
 * @date ： 2022-7-22
 */
@RestController
@RequestMapping("/sys")
public class LoginController {

    @Resource
    private UserService userService;
    @Resource
    private UserUtils userUtils;
    @Resource
    private MenuService menuService;

    /**
     * 登录接口
     *
     * @param loginUserDto
     */
    @PostMapping("/login")
    @LoginRequired
    public BaseResponse<UserDto> login(@RequestBody @Validated @NotNull LoginUserDto loginUserDto) {
        UserDto userDto = userService.login(loginUserDto);
        return new BaseResponse<>(userDto);
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public BaseResponse<Void> logout() {
        userUtils.logout();
        return new BaseResponse<>();
    }

    /**
     * 获取用户菜单及权限：
     * 这里因为前端框架问题，先写死简单实现适配。后续有必要再优化，重构前端框架，并优化菜单策略。
     */
    @GetMapping("/menu/nav")
    public BaseResponse<SysMenuResDto> nav() {
        //获取当前用户信息
        UserDto userDto = userUtils.getUser();
        Set<String> permissions = userDto.getPermissions();


        //菜单
        List<MenuEntity> menuEntities = menuService.selectByPermissionIn(permissions);
        SysMenuDto sysMenuDto;
        List<SysMenuDto> sysMenuDtos = new ArrayList<>();
        for (MenuEntity menuEntity : menuEntities) {
            sysMenuDto = new SysMenuDto();
            BeanUtils.copyProperties(menuEntity, sysMenuDto);
            sysMenuDtos.add(sysMenuDto);
        }

        //构建反参
        SysMenuResDto sysMenuResDto = new SysMenuResDto();
        sysMenuResDto.setPermissions(permissions);
        sysMenuResDto.setMenuList(sysMenuDtos);
        return new BaseResponse<>(sysMenuResDto);
    }


}
