package com.echo.common.web.response;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author echo
 */
@ToString
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class BaseResponse<T> {

    private static final long serialVersionUID = 1L;

    /**
     * 请求返回code
     */
    private int code;

    /**
     * 请求返回消息，成功为成功，失败返回失败原因
     */
    private String message;

    /**
     * 请求线程栈信息，异常时返回，用于调试
     */
    private String traceStack;

    /**
     * 异常说明，异常时返回，用于调试
     */
    private String exMessage;

    private T data;

    /**
     * 默认正确返回
     * 无返回结果
     */
    public BaseResponse() {
        this(ResultCode.SUCCESS, null, null, (Object[]) null);
    }


    /**
     * 正确返回
     */
    public BaseResponse(T data) {
        this(ResultCode.SUCCESS, data, null, (Object[]) null);
    }

    /**
     * 正确返回
     */
    public BaseResponse(ResultCode resultCode, Object... args) {
        this(resultCode, null, null, args);
    }

    /**
     * 正确返回，自定义message，code为200
     *
     * @param data
     */
    public BaseResponse(ResultCode resultCode, T data, Object... args) {
        this(resultCode, data, null, args);
    }

    /**
     * 异常构造
     *
     * @param ex
     */
    public BaseResponse(ResultCode resultCode, Throwable ex, Object... args) {
        this(resultCode, null, ex, args);
    }

    /**
     * 异常构造
     *
     * @param ex
     */
    public BaseResponse(@NotNull ResultCode resultCode, T data, Throwable ex, Object... args) {
        this.code = resultCode.getCode();
        this.message = MessageFormat.format(resultCode.getMessage(), args);
        this.data = data;
        if (ex != null) {
            this.traceStack = ExceptionUtil.stacktraceToString(ex, 1000);
            this.exMessage = ex.getMessage();
        }
    }

    //region 弃用区 直接指定code和message的方法都应该避免使用，要用统一枚举维护

    /**
     * 直接指定code和message的方法都应该避免使用，要用统一枚举维护
     *
     * @param code
     * @param message
     */
    @Deprecated
    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 直接指定code和message的方法都应该避免使用，要用统一枚举维护
     *
     * @param code
     * @param message
     * @param data
     */
    @Deprecated
    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 直接指定code和message的方法都应该避免使用，要用统一枚举维护
     * 正确返回，自定义message，code为200
     *
     * @param message
     * @param data
     */
    @Deprecated
    public BaseResponse(String message, T data) {
        this(ResultCode.SUCCESS.getCode(), message, data);
    }
    //endregion 弃用区

}
