package com.echo.common.user.autoconfigure;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created with IntelliJ IDEA.
 * Author: echo
 * Date: 2017/9/4
 * Time: 15:05
 * Describe: 自动化配置
 */
@EnableAsync
@Configuration
@MapperScan(basePackages = "com.echo.common.user.**.mapper")
@ComponentScan(basePackages = "com.echo.common.user")
public class UserAutoConfiguration {}
