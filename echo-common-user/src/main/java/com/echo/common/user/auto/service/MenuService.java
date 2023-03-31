package com.echo.common.user.auto.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.echo.common.user.auto.entity.MenuEntity;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author ： Echo
 * @version : 1.0
 * @date ： 2022-7-30
 */
public interface MenuService extends IService<MenuEntity> {


    int updateBatch(List<MenuEntity> list);

    int batchInsert(List<MenuEntity> list);

    int insertOrUpdate(MenuEntity record);

    int insertOrUpdateSelective(MenuEntity record);

    List<MenuEntity> selectByPermissionIn(Set<String> permissions);

}
