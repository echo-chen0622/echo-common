package com.echo.common.security.config;

import com.echo.common.security.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author Echo
 */
@Configuration
public class LoginConfig implements WebMvcConfigurer {
    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                //拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
                .addPathPatterns("/**");
    }
}
