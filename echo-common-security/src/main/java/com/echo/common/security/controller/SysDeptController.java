package com.echo.common.security.controller;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.echo.common.core.constant.UserConstants;
import com.echo.common.security.entity.SysDept;
import com.echo.common.security.enums.BusinessType;
import com.echo.common.security.service.SysDeptService;
import com.echo.common.web.response.BaseResponse;
import lombok.extern.java.Log;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门信息
 *
 * @author echo
 */
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {
    @Autowired
    private SysDeptService deptService;

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    BaseResponse<Object> list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return BaseResponse.ok(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    BaseResponse<Object> excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        depts.removeIf(d -> d.getDeptId().intValue() == deptId || ArrayUtils.contains(CharSequenceUtil.splitToArray(d.getAncestors(), ","), String.valueOf(deptId)));
        return BaseResponse.ok(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    BaseResponse<Object> getInfo(@PathVariable Long deptId) {
        deptService.checkDeptDataScope(deptId);
        return BaseResponse.ok(deptService.selectDeptById(deptId));
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
//    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    BaseResponse<Object> add(@Validated @RequestBody SysDept dept) {
        if (!deptService.checkDeptNameUnique(dept)) {
            return BaseResponse.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(getUsername());
        return BaseResponse.ok(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
//    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    BaseResponse<Object> edit(@Validated @RequestBody SysDept dept) {
        Long deptId = dept.getDeptId();
        deptService.checkDeptDataScope(deptId);
        if (!deptService.checkDeptNameUnique(dept)) {
            return BaseResponse.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(deptId)) {
            return BaseResponse.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (CharSequenceUtil.equals(UserConstants.DEPT_DISABLE, dept.getStatus()) && deptService.selectNormalChildrenDeptById(deptId) > 0) {
            return BaseResponse.error("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(getUsername());
        return BaseResponse.ok(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
//    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    BaseResponse<Object> remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return BaseResponse.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return BaseResponse.error("部门存在用户,不允许删除");
        }
        deptService.checkDeptDataScope(deptId);
        return BaseResponse.ok(deptService.deleteDeptById(deptId));
    }
}
