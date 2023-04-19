package com.echo.common.security.controller;

import cn.hutool.core.text.CharSequenceUtil;
import com.echo.common.security.entity.SysDept;
import com.echo.common.security.entity.SysRole;
import com.echo.common.security.entity.SysUser;
import com.echo.common.security.service.SysDeptService;
import com.echo.common.security.service.SysRoleService;
import com.echo.common.security.service.SysUserService;
import com.echo.common.security.utils.SecurityUtils;
import com.echo.common.web.page.TableDataInfo;
import com.echo.common.web.response.BaseResponse;
import com.echo.common.web.utils.poi.ExcelUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author echo
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysDeptService deptService;

    /**
     * 获取用户列表
     */
//    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    //   @Log(title = "用户管理", businessType = BusinessType.EXPORT)
//    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    //   @Log(title = "用户管理", businessType = BusinessType.IMPORT)
//    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    BaseResponse<Object> importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return BaseResponse.ok(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = {"/", "/{userId}"})
    BaseResponse<Object> getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        userService.checkUserDataScope(userId);
        Map<String, Object> ajax = new HashMap<>();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        if (userId != null) {
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put("data", sysUser);
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        return BaseResponse.ok(ajax);
    }

    /**
     * 新增用户
     */
//    @PreAuthorize("@ss.hasPermi('system:user:add')")
//   @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    BaseResponse<Object> add(@Validated @RequestBody SysUser user) {
        if (!userService.checkUserNameUnique(user)) {
            return BaseResponse.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (CharSequenceUtil.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return BaseResponse.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (CharSequenceUtil.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return BaseResponse.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return BaseResponse.ok(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
//    @PreAuthorize("@ss.hasPermi('system:user:edit')")
//   @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    BaseResponse<Object> edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (!userService.checkUserNameUnique(user)) {
            return BaseResponse.error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (CharSequenceUtil.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return BaseResponse.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (CharSequenceUtil.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return BaseResponse.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(getUsername());
        return BaseResponse.ok(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
//    @PreAuthorize("@ss.hasPermi('system:user:remove')")
//   @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    BaseResponse<Object> remove(@PathVariable Long[] userIds) {
        if (ArrayUtils.contains(userIds, getUserId())) {
            return BaseResponse.error("当前用户不能删除");
        }
        return BaseResponse.ok(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
//    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
//   @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    BaseResponse<Object> resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return BaseResponse.ok(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
//    @PreAuthorize("@ss.hasPermi('system:user:edit')")
//   @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    BaseResponse<Object> changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(getUsername());
        return BaseResponse.ok(userService.updateUserStatus(user));
    }

    /**
     * 根据用户编号获取授权角色
     */
//    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("/authRole/{userId}")
    BaseResponse<Object> authRole(@PathVariable("userId") Long userId) {
        Map<String, Object> ajax = new HashMap<>();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return BaseResponse.ok(ajax);
    }

    /**
     * 用户授权角色
     */
//    @PreAuthorize("@ss.hasPermi('system:user:edit')")
//   @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    BaseResponse<Object> insertAuthRole(Long userId, Long[] roleIds) {
        userService.checkUserDataScope(userId);
        userService.insertUserAuth(userId, roleIds);
        return BaseResponse.ok();
    }

    /**
     * 获取部门树列表
     */
//    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/deptTree")
    BaseResponse<Object> deptTree(SysDept dept) {
        return BaseResponse.ok(deptService.selectDeptTreeList(dept));
    }
}
