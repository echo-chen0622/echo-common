package com.echo.common.user.auto.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.echo.common.user.auto.entity.MenuEntity;
import com.echo.common.user.auto.mapper.MenuMapper;
import com.echo.common.user.auto.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author ： Echo
 * @version : 1.0
 * @date ： 2022-7-30
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuEntity> implements MenuService {

    @Override
    public int updateBatch(List<MenuEntity> list) {
        return baseMapper.updateBatch(list);
    }

    @Override
    public int batchInsert(List<MenuEntity> list) {
        return baseMapper.batchInsert(list);
    }

    @Override
    public int insertOrUpdate(MenuEntity record) {
        return baseMapper.insertOrUpdate(record);
    }

    @Override
    public int insertOrUpdateSelective(MenuEntity record) {
        return baseMapper.insertOrUpdateSelective(record);
    }

    @Override
    public List<MenuEntity> selectByPermissionIn(Set<String> permissions) {
        return baseMapper.selectByPermissionIn(permissions);
    }
}
