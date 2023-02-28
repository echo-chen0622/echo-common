package com.echo.common.swagger.annotation;

import com.echo.common.swagger.config.SwaggerAutoConfiguration;
import com.echo.common.swagger.support.SwaggerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启 echo spring doc
 *
 * @author echo
 * @date 2022-03-26
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableConfigurationProperties(SwaggerProperties.class)
@Import({SwaggerAutoConfiguration.class})
public @interface EnableEchoDoc {

}
