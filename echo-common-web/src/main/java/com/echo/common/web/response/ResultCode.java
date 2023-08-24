package com.echo.common.web.response;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResultCode {
    // 成功业务码
    SUCCESS("成功", 200),

    // 公共错误码
    DEFAULT_ERROR("{0}", 500),
    INTERNAL_ERROR("系统内部异常，请联系系统管理员", 500),
    INVALID_ARGUMENT("invalid_argument", 400),
    REGISTERED_MOBILE("registered_mobile", 400),
    METHOD_NOT_ALLOWED("method_not_allowed", 405),
    PARSE_ERROR("parse_error:{0}", 400),
    GEN_WORK_ID_ERROR("worker Id 不能大于 {0} 获取小于 0", 500),
    GEN_CENTER_ID_ERROR("center Id 不能大于 {0} 获取小于 0", 500),
    GEN_CLOCK_BACKWARDS_ERROR("时钟回拨 请等待: {0} ms 之后再次尝试获取id ", 500),
    /**
     * 单元格校验不通过
     */
    FILE_NOT_FIND_ERROR("无法找到对应文件，请刷新页面重试", 500),
    APP_NOT_FIND_ERROR("{0}系统未激活，请联系系统管理员", 500),
    FILEPATH_NOT_FIND_ERROR("{0}文件路径异常，请联系系统管理员", 500),
    UPLOAD_NULL_ERROR("上传文件不能为空", 500),
    LOGIN_ERROR("登录异常，请重新登录", 303),
    LOGIN_PASS_ERROR("密码错误，还可以再试{0}次", 303),
    TOKEN_EMPTY("登录异常，TOKEN为空，请重新登录", 303),
    CAPTCHA_EXPIRED("验证码异常校验失败", 304),
    RETRY_EXPIRED("密码错误超过{0}次数，请{1}后再试", 304),
    CALC_CITE_ERROR("计算{0}时，计算式{1}，其引用的变量{2}{5}，{2}的值：{3}，{2}的配置：{4}", 500),
    CALC_ERROR("计算{0}时，配置计算式{1}，最终计算式{2}值不是数字", 500)
    ;

    private final String message;
    private final int code;


    ResultCode(String message, int code) {
        this.message = message;
        this.code = code;
    }

    @JsonValue
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
