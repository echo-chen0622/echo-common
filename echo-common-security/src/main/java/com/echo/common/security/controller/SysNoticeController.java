package com.echo.common.security.controller;

import com.echo.common.security.entity.SysNotice;
import com.echo.common.security.service.SysNoticeService;
import com.echo.common.web.page.TableDataInfo;
import com.echo.common.web.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告 信息操作处理
 *
 * @author echo
 */
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {
    @Autowired
    private SysNoticeService noticeService;

    /**
     * 获取通知公告列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice) {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/{noticeId}")
    BaseResponse<Object> getInfo(@PathVariable Long noticeId) {
        return BaseResponse.ok(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
//   @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    BaseResponse<Object> add(@Validated @RequestBody SysNotice notice) {
        notice.setCreateBy(getUsername());
        return BaseResponse.ok(noticeService.insertNotice(notice));
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
//   @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    BaseResponse<Object> edit(@Validated @RequestBody SysNotice notice) {
        notice.setUpdateBy(getUsername());
        return BaseResponse.ok(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
//   @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    BaseResponse<Object> remove(@PathVariable Long[] noticeIds) {
        return BaseResponse.ok(noticeService.deleteNoticeByIds(noticeIds));
    }
}
