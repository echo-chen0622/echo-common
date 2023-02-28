package com.echo.common.mybatis.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.echo.common.core.util.ClassUtils;
import com.echo.common.core.util.SpringContextHolder;
import com.echo.common.mybatis.CodeAutoGen;
import com.echo.common.security.dto.UserDto;
import com.echo.common.security.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

/**
 * MybatisPlus 自动填充配置
 *
 * @author echo
 */
@Slf4j
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    /**
     * 填充值，先判断是否有手动设置，优先手动设置的值，例如：job必须手动设置
     *
     * @param fieldName  属性名
     * @param fieldVal   属性值
     * @param metaObject MetaObject
     * @param isCover    是否覆盖原有值,避免更新操作手动入参
     */
    private static void fillValIfNullByName(String fieldName, Object fieldVal, MetaObject metaObject, boolean isCover) {
        // 1. 没有 set 方法
        if (!metaObject.hasSetter(fieldName)) {
            return;
        }
        // 2. 如果用户有手动设置的值
        Object userSetValue = metaObject.getValue(fieldName);
        String setValueStr = StrUtil.str(userSetValue, Charset.defaultCharset());
        if (CharSequenceUtil.isNotBlank(setValueStr) && !isCover) {
            return;
        }
        // 3. field 类型相同时设置
        Class<?> getterType = metaObject.getGetterType(fieldName);
        if (ClassUtils.isAssignableValue(getterType, fieldVal)) {
            metaObject.setValue(fieldName, fieldVal);
        }
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("mybatis plus start insert fill ....");
        LocalDateTime now = LocalDateTime.now();


        Field[] fields = ClassUtils.getAllFields(metaObject.getOriginalObject().getClass());
        for (Field field : fields) {
            CodeAutoGen annotation = field.getAnnotation(CodeAutoGen.class);
            if (annotation != null) {
                //有自动生成 code 注解的情况下，自动生产注解
                String fieldName = field.getName();
                //参数1为终端ID
                //参数2为数据中心ID
                Snowflake snowflake = IdUtil.getSnowflake();
                long id = snowflake.nextId();
                fillValIfNullByName(fieldName, String.valueOf(id), metaObject, false);
            }
        }

        fillValIfNullByName("createTime", now, metaObject, false);
        fillValIfNullByName("updateTime", now, metaObject, false);
        fillValIfNullByName("createBy", getUserName(), metaObject, false);
        fillValIfNullByName("updateBy", getUserName(), metaObject, false);
        fillValIfNullByName("delFlag", Byte.valueOf("0"), metaObject, false);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("mybatis plus start update fill ....");
        fillValIfNullByName("updateTime", LocalDateTime.now(), metaObject, true);
        fillValIfNullByName("updateBy", getUserName(), metaObject, true);
    }

    /**
     * 获取 spring security 当前的用户名
     *
     * @return 当前用户名
     */
    private String getUserName() {
        UserUtils userUtils = SpringContextHolder.getApplicationContext().getBean(UserUtils.class);
        UserDto user = userUtils.getUser();
        if (user != null) {
            return user.getLoginName();
        }
        return null;
    }

}
