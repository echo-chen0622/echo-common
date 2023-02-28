package com.echo.common.web.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.echo.common.web.response.BaseResponse;
import com.echo.common.web.response.ResultCode;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private final Throwables.AnalyzerBuilder analyzerBuilder;

    //国际化不好用，需要创建国际化配置。暂不考虑国际化功能
    //    @Autowired
    //    private MessageSource messages;

    /**
     *
     */
    public RestResponseExceptionHandler() {
        super();
        this.analyzerBuilder = Throwables.builder(ServletException.class, e -> ((ServletException) e).getRootCause());
    }

    /**
     *
     */
    @Override
    protected @NotNull ResponseEntity<Object> handleBindException(final @NotNull BindException ex, final @NotNull HttpHeaders headers, final @NotNull HttpStatus status, final WebRequest request) {
        final List<ObjectError> result = ex.getBindingResult().getAllErrors();
        Map<String, String> fields = result.stream().collect(Collectors.toMap(e -> e instanceof FieldError ? ((FieldError) e).getField() : e.getObjectName(), ObjectError::getDefaultMessage));
        return handle(ex, request, ResultCode.INVALID_ARGUMENT, fields);
    }

    @Override
    protected @NotNull ResponseEntity<Object> handleMethodArgumentNotValid(final @NotNull MethodArgumentNotValidException ex,
                                                                           final HttpHeaders headers,
                                                                           final HttpStatus status,
                                                                           final WebRequest request
                                                                          ) {
        final List<ObjectError> result = ex.getBindingResult().getAllErrors();
        Map<String, String> fields = result.stream().collect(Collectors.toMap(e -> e instanceof FieldError ? ((FieldError) e).getField() : e.getObjectName(), ObjectError::getDefaultMessage));
        return handle(ex, request, ResultCode.INVALID_ARGUMENT, fields);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
        final Set<ConstraintViolation<?>> result = ex.getConstraintViolations();
        Map<String, String> fields = result.stream().collect(Collectors.toMap(e -> e.getPropertyPath().toString(), e -> e.getMessageTemplate().replaceAll("[{}]", "")));
        return handle(ex, request, ResultCode.INVALID_ARGUMENT, fields);
    }

    /**
     * 自定义业务异常捕获，这个很重要
     *
     * @param ex
     * @param request
     */
    @ExceptionHandler({ApiException.class})
    public ResponseEntity<Object> handleGeneric(final ApiException ex, final WebRequest request) {
        return handle(ex, request, ex.getErrorCode(), null, ex.getArgs());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleInternal(final Exception ex, final WebRequest request) {
        Throwables.Analyzer analyzer = analyzerBuilder.build(ex);

        Throwable cause = analyzer.getFirstThrowableOfType(ApiException.class);
        if (cause != null) {return handleGeneric((ApiException) cause, request);}

        cause = analyzer.getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class);
        if (cause != null) {return handle(ex, request, ResultCode.METHOD_NOT_ALLOWED, null);}

        return handle(ex, request, ResultCode.INTERNAL_ERROR, null);
    }

    private ResponseEntity<Object> handle(Exception ex, WebRequest request, ResultCode code, Object payload, Object... args) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CACHE_CONTROL, "no-store");
        headers.set(HttpHeaders.PRAGMA, "no-cache");
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        BaseResponse<Object> response = new BaseResponse<>(code, ex, args);
        try {
            if (payload == null) {
                payload = MessageFormat.format(code.getMessage(), args);
            }
            log.info("Error response: exMessage: {}, exception: {}", payload, ExceptionUtil.stacktraceToString(ex));
            response.setExMessage(payload.toString());
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An unexpected exception occurred while handle error response: " + code.getMessage(), e);
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }
    }
}
