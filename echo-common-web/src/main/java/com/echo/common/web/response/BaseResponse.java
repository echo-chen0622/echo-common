package com.echo.common.web.response;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

/**
 * 响应信息主体
 *
 * @param <T>
 *
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

    @Contract(" -> new")
    public static <T> @NotNull BaseResponse<T> ok() {
        return new BaseResponse<>();
    }

    @Contract("_ -> new")
    public static <T> @NotNull BaseResponse<T> ok(T data) {
        return new BaseResponse<>(data);
    }

    @Contract("_, _ -> new")
    public static <T> @NotNull BaseResponse<T> ok(ResultCode resultCode, Object... args) {
        return new BaseResponse<>(resultCode, args);
    }

    @Contract("_, _, _ -> new")
    public static <T> @NotNull BaseResponse<T> ok(ResultCode resultCode, T data, Object... args) {
        return new BaseResponse<>(resultCode, data, args);
    }

    @Contract("_, _ -> new")
    public static <T> @NotNull BaseResponse<T> error(ResultCode resultCode, Object... args) {
        return new BaseResponse<>(resultCode, args);
    }

    @Contract("_ -> new")
    public static <T> @NotNull BaseResponse<T> error(Object... args) {
        return new BaseResponse<>(ResultCode.DEFAULT_ERROR, args);
    }

    @Contract("_, _, _ -> new")
    public static <T> @NotNull BaseResponse<T> error(ResultCode resultCode, Throwable ex, Object... args) {
        return new BaseResponse<>(resultCode, ex, args);
    }

    @Contract("_, _, _, _ -> new")
    public static <T> @NotNull BaseResponse<T> error(ResultCode resultCode, T data, Throwable ex, Object... args) {
        return new BaseResponse<>(resultCode, data, ex, args);
    }

    @Contract("_ -> new")
    public static <T> @NotNull BaseResponse<T> error(Throwable ex) {
        return new BaseResponse<>(ResultCode.INTERNAL_ERROR, ex);
    }

}
