package com.echo.common.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author chentiancheng
 * @date 2020/2/14
 * @time 18:34
 * @describe
 */
@Configuration
@Order(1)
public class HttpLogFilter extends CommonsRequestLoggingFilter {

    @Value("${logging.request.enable:true}")
    private boolean enable;

    public HttpLogFilter() {
        super.setIncludeQueryString(true);
        super.setIncludeClientInfo(true);
        super.setIncludeHeaders(true);
        super.setIncludePayload(true);
        super.setMaxPayloadLength(5120);
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return enable;
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.info(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.info(message);
    }
}
