package com.echo.common.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动生产 code 注解。上注解的字段，会利用雪花算法，配合TableField fill属性。自动赋值
 * 被标记的字段需要为 String (设计缺陷，后续可以更改为 Long
 *
 * @author echo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CodeAutoGen {}
