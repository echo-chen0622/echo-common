package com.echo.common.user.auto.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.echo.common.user.auto.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author ： Echo
 * @version : 1.0
 * @date ： 2022-7-30
 */
@Mapper
public interface MenuMapper extends BaseMapper<MenuEntity> {
    int updateBatch(List<MenuEntity> list);

    int batchInsert(@Param("list") List<MenuEntity> list);

    int insertOrUpdate(MenuEntity record);

    int insertOrUpdateSelective(MenuEntity record);

    List<MenuEntity> selectByPermissionIn(@Param("permissions") Set<String> permissions);
}
