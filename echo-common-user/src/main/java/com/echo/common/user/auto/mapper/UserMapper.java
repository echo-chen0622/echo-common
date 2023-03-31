package com.echo.common.user.auto.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.echo.common.user.auto.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author ： Echo
 * @version : 1.0
 * @date ： 2022-7-26
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
    int updateBatch(List<UserEntity> list);

    int batchInsert(@Param("list") List<UserEntity> list);

    int insertOrUpdate(UserEntity record);

    int insertOrUpdateSelective(UserEntity record);

    UserEntity selectByLogin(@Param("loginName") String loginName, @Param("password") String password);
}
