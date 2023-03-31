package com.echo.common.user.auto.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.echo.common.core.util.SpringContextHolder;
import com.echo.common.security.dto.LoginUserDto;
import com.echo.common.security.dto.UserDto;
import com.echo.common.security.util.UserUtils;
import com.echo.common.user.auto.entity.UserEntity;
import com.echo.common.user.auto.mapper.UserMapper;
import com.echo.common.user.auto.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author ： Echo
 * @version : 1.0
 * @date ： 2022-7-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Resource
    private UserUtils userUtils;

    @Override
    public int updateBatch(List<UserEntity> list) {
        return baseMapper.updateBatch(list);
    }

    @Override
    public int batchInsert(List<UserEntity> list) {
        return baseMapper.batchInsert(list);
    }

    @Override
    public int insertOrUpdate(UserEntity record) {
        return baseMapper.insertOrUpdate(record);
    }

    @Override
    public int insertOrUpdateSelective(UserEntity record) {
        return baseMapper.insertOrUpdateSelective(record);
    }

    @Override
    public UserDto login(@NotNull LoginUserDto userDto) {
        SpringContextHolder.getBean(UserMapper.class);
        UserEntity userEntity = baseMapper.selectByLogin(userDto.getLoginName(), userDto.getPassword());
        UserDto user = new UserDto();
        user.setLoginName(userEntity.getLoginName());
        user.setMobile(userEntity.getMobile());
        user.setRealName(userEntity.getRealName());
        user.setPermissions(new HashSet<>(JSONUtil.toList(userEntity.getPermissions(), String.class)));
        return userUtils.login(user);

    }


}
