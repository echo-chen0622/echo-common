package com.echo.common.security.autoconfigure;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.echo.common.security.handle.MybatisPlusMetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@MapperScan(basePackages = {"com.echo.common.security.**.mapper"})
@ComponentScan(basePackages = "com.echo.common.security")
public class AutoConfigure {

    /**
     * 审计字段自动填充
     *
     * @return {@link MetaObjectHandler}
     */
    @Bean
    public MybatisPlusMetaObjectHandler mybatisPlusMetaObjectHandler() {
        return new MybatisPlusMetaObjectHandler();
    }

}
