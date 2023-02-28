package com.echo.common.web.exception;

import com.echo.common.web.response.ResultCode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

public final class ApiException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final ResultCode resultCode;
    private final transient Object[] args;

    /**
     * 这里提供一个默认异常抛出方案，不需要管理异常Code时可以用这种构造方法
     * 不推荐使用，因为这样会导致异常混乱：相同业务场景下，异常Code不同
     * 不提供自定义异常code的构造方法，因为通常情况下，自定义异常都有其业务属性，自定义构造会使得代码内极难维护
     *
     * @param errorText
     * @param args
     */
    public ApiException(@NotNull String errorText, Object... args) {
        super(errorText = MessageFormat.format(errorText, args));
        this.resultCode = ResultCode.DEFAULT_ERROR;
        this.args = new String[]{errorText};
    }

    public ApiException(@NotNull ResultCode resultCode, Object... args) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
        this.args = args;
    }

    public ApiException(@NotNull ResultCode resultCode, Throwable e, Object... args) {
        super(resultCode.getMessage(), e);
        this.resultCode = resultCode;
        this.args = args;
    }

    @Contract(pure = true)
    public ResultCode getErrorCode() {
        return resultCode;
    }

    @Contract(pure = true)
    public Object[] getArgs() {
        return args;
    }

}
