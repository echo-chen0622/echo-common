package com.echo.common.user.auto.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.echo.common.security.dto.LoginUserDto;
import com.echo.common.security.dto.UserDto;
import com.echo.common.user.auto.entity.UserEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author ： Echo
 * @version : 1.0
 * @date ： 2022-7-26
 */
public interface UserService extends IService<UserEntity> {


    int updateBatch(List<UserEntity> list);

    int batchInsert(List<UserEntity> list);

    int insertOrUpdate(UserEntity record);

    int insertOrUpdateSelective(UserEntity record);

    /**
     * 登录
     *
     * @param userDto
     */
    UserDto login(LoginUserDto userDto);
}
