package com.echo.common.security.interceptor;

import com.echo.common.security.LoginRequired;
import com.echo.common.security.dto.UserDto;
import com.echo.common.security.util.UserUtils;
import com.echo.common.web.exception.ApiException;
import com.echo.common.web.response.ResultCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Log4j2
@Component
public class LoginInterceptor implements AsyncHandlerInterceptor {

    @Resource
    private UserUtils userUtils;

    /**
     * 在请求处理之前进行调用（Controller方法调用之前） 基于URL实现的拦截器
     *
     * @param request
     * @param response
     * @param handler
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 判断接口是否需要登录认证
        LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
        if (loginRequired != null) {
            // 有 @LoginRequired 注解，需要认证
            return true;
        }

        //获取用户信息
        UserDto user = userUtils.getUser();
        if (user == null) {
            throw new ApiException(ResultCode.LOGIN_ERROR);
        }
        return true;
    }

}
