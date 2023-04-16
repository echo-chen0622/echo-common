package com.echo.common.security.enums;

import com.echo.common.core.enums.BaseEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户状态
 *
 * @author echo
 */
@Getter
public enum UserStatus implements BaseEnum {
    OK((byte) 0, "正常"), DISABLE((byte) 1, "停用"), DELETED((byte) 2, "删除");

    private final Byte code;
    private final String value;

    UserStatus(Byte code, String value) {
        this.code = code;
        this.value = value;
    }

}
