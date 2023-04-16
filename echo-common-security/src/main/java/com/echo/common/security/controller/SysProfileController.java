package com.echo.common.security.controller;

import cn.hutool.core.text.CharSequenceUtil;
import com.echo.common.security.entity.LoginUser;
import com.echo.common.security.entity.SysUser;
import com.echo.common.security.service.SysUserService;
import com.echo.common.security.utils.SecurityUtils;
import com.echo.common.security.utils.UserUtils;
import com.echo.common.web.config.EchoConfig;
import com.echo.common.web.response.BaseResponse;
import com.echo.common.web.utils.file.FileUploadUtils;
import com.echo.common.web.utils.file.MimeTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人信息 业务处理
 *
 * @author echo
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {
    @Autowired
    private SysUserService userService;

    @Autowired
    private UserUtils userUtils;

    /**
     * 个人信息
     */
    @GetMapping
    BaseResponse<Object> profile() {
        LoginUser loginUser = getLoginUser();
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
        return BaseResponse.ok(ajax);
    }

    /**
     * 修改用户
     */
//   @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    BaseResponse<Object> updateProfile(@RequestBody SysUser user) {
        LoginUser loginUser = getLoginUser();
        SysUser sysUser = loginUser.getUser();
        user.setUserName(sysUser.getUserName());
        if (CharSequenceUtil.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return BaseResponse.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (CharSequenceUtil.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return BaseResponse.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUserId(sysUser.getUserId());
        user.setPassword(null);
        user.setAvatar(null);
        user.setDeptId(null);
        if (userService.updateUserProfile(user) > 0) {
            // 更新缓存用户信息
            sysUser.setNickName(user.getNickName());
            sysUser.setPhonenumber(user.getPhonenumber());
            sysUser.setEmail(user.getEmail());
            sysUser.setSex(user.getSex());
            userUtils.login(loginUser);
            return BaseResponse.ok();
        }
        return BaseResponse.error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
//   @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    BaseResponse<Object> updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = getLoginUser();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return BaseResponse.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return BaseResponse.error("新密码不能与旧密码相同");
        }
        if (userService.resetUserPwd(userName, SecurityUtils.encryptPassword(newPassword)) > 0) {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            userUtils.login(loginUser);
            return BaseResponse.ok();
        }
        return BaseResponse.error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
//   @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    BaseResponse<Object> avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {
            LoginUser loginUser = getLoginUser();
            String avatar = FileUploadUtils.upload(EchoConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
                Map<String, Object> ajax = new HashMap<>();
                ajax.put("imgUrl", avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                userUtils.login(loginUser);
                return BaseResponse.ok(ajax);
            }
        }
        return BaseResponse.error("上传图片异常，请联系管理员");
    }
}
