package com.echo.common.security.config;

import com.echo.common.security.interceptor.LoginInterceptor;
import com.echo.common.security.util.UserCache;
import com.echo.common.security.util.UserUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class AutoConfigure {

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Bean
    public LoginConfig loginConfig() {
        return new LoginConfig();
    }

    @Bean
    public UserCache userCache() {
        return new UserCache();
    }

    @Bean
    public UserUtils userUtils() {
        return new UserUtils();
    }

}
