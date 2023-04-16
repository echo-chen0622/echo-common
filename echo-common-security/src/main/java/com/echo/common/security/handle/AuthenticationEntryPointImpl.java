package com.echo.common.security.handle;

import cn.hutool.json.JSONUtil;
import com.echo.common.web.utils.StringUtils;
import com.echo.common.web.response.BaseResponse;
import com.echo.common.web.response.ResultCode;
import com.echo.common.web.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 *
 * @author echo
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        String msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        WebUtils.renderJson(response, JSONUtil.toJsonStr(BaseResponse.error(ResultCode.LOGIN_ERROR, msg)));
    }
}
