package com.echo.common.security.controller;


import com.echo.common.web.utils.poi.ExcelUtil;
import com.echo.common.security.entity.SysConfig;
import com.echo.common.security.service.SysConfigService;
import com.echo.common.web.page.TableDataInfo;
import com.echo.common.web.response.BaseResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.echo.common.web.response.BaseResponse.error;

/**
 * 参数配置 信息操作处理
 *
 * @author echo
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {
    @Resource
    private SysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysConfig config) {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysConfig config) {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<>(SysConfig.class);
        util.exportExcel(response, list, "参数数据");
    }

    /**
     * 根据参数编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{configId}")
    BaseResponse<Object> getInfo(@PathVariable Long configId) {
        return BaseResponse.ok(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    BaseResponse<Object> getConfigKey(@PathVariable String configKey) {
        return BaseResponse.ok(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:add')")
//    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    BaseResponse<Object> add(@Validated @RequestBody SysConfig config) {
        if (!configService.checkConfigKeyUnique(config)) {
            return BaseResponse.error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(getUsername());
        return BaseResponse.ok(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
//    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    BaseResponse<Object> edit(@Validated @RequestBody SysConfig config) {
        if (!configService.checkConfigKeyUnique(config)) {
            return BaseResponse.error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(getUsername());
        return BaseResponse.ok(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
//    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    BaseResponse<Object> remove(@PathVariable Long[] configIds) {
        configService.deleteConfigByIds(configIds);
        return BaseResponse.ok();
    }

    /**
     * 刷新参数缓存
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
//    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public BaseResponse<Void> refreshCache() {
        configService.resetConfigCache();
        return BaseResponse.ok();
    }
}
